package io.librevents.application.event.decoder.block;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.event.contract.parameter.*;
import io.librevents.domain.filter.event.EventFilterSpecification;
import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.parameter.ArrayParameterDefinition;
import io.librevents.domain.filter.event.parameter.BytesFixedParameterDefinition;
import io.librevents.domain.filter.event.parameter.StructParameterDefinition;

import static io.librevents.application.common.util.EncryptionUtil.bytesToHex;
import static io.librevents.application.common.util.EncryptionUtil.hexStringToByteArray;

public final class DefaultContractEventParameterDecoder implements ContractEventParameterDecoder {

    public Set<ContractEventParameter<?>> decode(
            EventFilterSpecification specification, String logData) {

        byte[] data = hexStringToByteArray(logData);
        int offset = 0;

        Set<ParameterDefinition> ordered =
                specification.parameters().stream()
                        .sorted(Comparator.comparingInt(ParameterDefinition::getPosition))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<ContractEventParameter<?>> result = new LinkedHashSet<>();

        for (ParameterDefinition def : ordered) {
            DecodeResult decoded = decodeParameter(def, data, offset);
            result.add(decoded.parameter());
            offset = decoded.newOffset();
        }
        return result;
    }

    public DecodeResult decodeParameter(ParameterDefinition definition, byte[] data, int offset) {
        return switch (definition.getType()) {
            case ADDRESS -> decodeAddress(data, offset, definition);
            case UINT -> decodeUint(data, offset, definition);
            case INT -> decodeInt(data, offset, definition);
            case BOOL -> decodeBool(data, offset, definition);
            case STRING -> decodeString(data, offset, definition);
            case BYTES -> decodeBytes(data, offset, definition);
            case BYTES_FIXED ->
                    decodeFixedBytes(data, offset, (BytesFixedParameterDefinition) definition);
            case ARRAY -> decodeArray(data, offset, (ArrayParameterDefinition) definition);
            case STRUCT -> decodeStruct(data, offset, (StructParameterDefinition) definition);
        };
    }

    private DecodeResult decodeAddress(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset + 12, offset + 32);
        String address = "0x" + bytesToHex(slice);
        return new DecodeResult(
                new AddressParameter(definition.isIndexed(), definition.getPosition(), address),
                offset + 32);
    }

    private DecodeResult decodeUint(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset, offset + 32);
        int value = new BigInteger(slice).intValue();
        return new DecodeResult(
                new UintParameter(definition.isIndexed(), definition.getPosition(), value),
                offset + 32);
    }

    private DecodeResult decodeInt(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset, offset + 32);
        int value = new BigInteger(slice).intValue(); // signed
        return new DecodeResult(
                new IntParameter(definition.isIndexed(), definition.getPosition(), value),
                offset + 32);
    }

    private DecodeResult decodeBool(byte[] data, int offset, ParameterDefinition definition) {
        byte value = data[offset + 31];
        return new DecodeResult(
                new BoolParameter(definition.isIndexed(), definition.getPosition(), value == 1),
                offset + 32);
    }

    private DecodeResult decodeString(byte[] data, int offset, ParameterDefinition definition) {
        int dynOffset = asInt(data, offset);
        int length = asInt(data, dynOffset);
        byte[] strBytes = Arrays.copyOfRange(data, dynOffset + 32, dynOffset + 32 + length);
        return new DecodeResult(
                new StringParameter(
                        definition.isIndexed(),
                        definition.getPosition(),
                        new String(strBytes, StandardCharsets.UTF_8)),
                offset + 32);
    }

    private DecodeResult decodeBytes(byte[] data, int offset, ParameterDefinition definition) {
        int dynOffset = asInt(data, offset);
        int length = asInt(data, dynOffset);
        byte[] rawBytes = Arrays.copyOfRange(data, dynOffset + 32, dynOffset + 32 + length);
        return new DecodeResult(
                new BytesParameter(definition.isIndexed(), definition.getPosition(), rawBytes),
                offset + 32);
    }

    private DecodeResult decodeFixedBytes(
            byte[] data, int offset, BytesFixedParameterDefinition definition) {
        byte[] fixed = Arrays.copyOfRange(data, offset, offset + definition.getByteLength());
        return new DecodeResult(
                new BytesFixedParameter(
                        definition.isIndexed(),
                        definition.getPosition(),
                        fixed,
                        definition.getByteLength()),
                offset + 32);
    }

    private DecodeResult decodeArray(byte[] data, int offset, ArrayParameterDefinition definition) {
        int headWord = asInt(data, offset);

        ParameterDefinition elementDef = definition.getElementType();

        List<ContractEventParameter<?>> items = new ArrayList<>();

        if (definition.getFixedLength() == null) {

            int length = asInt(data, headWord);

            int current = headWord + 32;
            for (int i = 0; i < length; i++) {
                DecodeResult elt = decodeParameter(elementDef, data, current);
                items.add(elt.parameter());
                current = elt.newOffset();
            }

            return new DecodeResult(
                    new ArrayParameter<>(definition.isIndexed(), definition.getPosition(), items),
                    offset + 32);

        } else {
            int length = definition.getFixedLength();
            int current = offset;
            for (int i = 0; i < length; i++) {
                DecodeResult elt = decodeParameter(elementDef, data, current);
                items.add(elt.parameter());
                current = elt.newOffset();
            }

            return new DecodeResult(
                    new ArrayParameter<>(definition.isIndexed(), definition.getPosition(), items),
                    current);
        }
    }

    private DecodeResult decodeStruct(
            byte[] data, int offset, StructParameterDefinition definition) {
        List<ContractEventParameter<?>> values = new ArrayList<>();
        int current = offset;
        for (ParameterDefinition type : definition.getParameterDefinitions()) {
            DecodeResult res = decodeParameter(type, data, current);
            values.add(res.parameter());
            current = res.newOffset();
        }
        return new DecodeResult(
                new StructParameter(definition.isIndexed(), definition.getPosition(), values),
                current);
    }

    private static int asInt(byte[] data, int offset) {
        byte[] slice = Arrays.copyOfRange(data, offset + 28, offset + 32);
        return ByteBuffer.wrap(slice).getInt();
    }
}
