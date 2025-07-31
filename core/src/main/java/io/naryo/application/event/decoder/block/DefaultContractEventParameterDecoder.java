package io.naryo.application.event.decoder.block;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.*;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.ArrayParameterDefinition;
import io.naryo.domain.filter.event.parameter.BytesFixedParameterDefinition;
import io.naryo.domain.filter.event.parameter.StructParameterDefinition;

import static io.naryo.application.common.util.EncryptionUtil.arrayify;
import static io.naryo.application.common.util.EncryptionUtil.hexlify;

public final class DefaultContractEventParameterDecoder implements ContractEventParameterDecoder {

    private static int asInt(byte[] data, int offset) {
        byte[] slice = Arrays.copyOfRange(data, offset + 28, offset + 32);
        return ByteBuffer.wrap(slice).getInt();
    }

    public Set<ContractEventParameter<?>> decode(EventFilterSpecification specification, Log log) {

        byte[] data = arrayify(log.data());
        int offset = 0;

        Set<ParameterDefinition> ordered =
                specification.parameters().stream()
                        .sorted(Comparator.comparingInt(ParameterDefinition::getPosition))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<ContractEventParameter<?>> result = new LinkedHashSet<>();

        int topicCount = 0;
        for (ParameterDefinition def : ordered) {
            if (def.isIndexed()) {
                int topicPosition = topicCount++;
                ContractEventParameter<?> value =
                        switch (def.getType()) {
                            case ADDRESS ->
                                    new AddressParameter(
                                            true,
                                            def.getPosition(),
                                            log.topics().get(topicPosition));
                            case STRING, BYTES, BYTES_FIXED ->
                                    new StringParameter(
                                            true,
                                            def.getPosition(),
                                            log.topics().get(topicPosition));
                            case UINT, INT, BOOL -> decodeParameter(def, data, 0).parameter();
                            default ->
                                    throw new IllegalStateException(
                                            "Unexpected value: " + def.getType());
                        };
                result.add(value);
                continue;
            }
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
        String address = hexlify(slice);
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
}
