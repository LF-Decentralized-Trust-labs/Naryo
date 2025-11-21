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

    static final int WORD = 32;

    private static int asInt(byte[] data, int offset) {
        byte[] slice = Arrays.copyOfRange(data, offset + 28, offset + WORD);
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
                String topicStr = log.topics().get(++topicCount);
                byte[] topic = arrayify(topicStr);
                ContractEventParameter<?> value = switch (def.getType()) {
                    case BYTES -> new BytesParameter(def.isIndexed(), def.getPosition(), topic);
                    case STRING -> new StringParameter(def.isIndexed(), def.getPosition(), topicStr);
                    default -> decodeParameter(def, topic, 0, 0).parameter();
                };
                result.add(value);
                continue;
            }
            DecodeResult decoded = decodeParameter(def, data, offset, 0);
            result.add(decoded.parameter());
            offset = decoded.newOffset();
        }
        return result;
    }

    public DecodeResult decodeParameter(
            ParameterDefinition definition, byte[] data, int offset, int baseHead) {
        return switch (definition.getType()) {
            case ADDRESS -> decodeAddress(data, offset, definition);
            case UINT -> decodeUint(data, offset, definition);
            case INT -> decodeInt(data, offset, definition);
            case BOOL -> decodeBool(data, offset, definition);
            case STRING -> decodeString(data, offset, baseHead, definition);
            case BYTES -> decodeBytes(data, offset, baseHead, definition);
            case BYTES_FIXED ->
                    decodeFixedBytes(data, offset, (BytesFixedParameterDefinition) definition);
            case ARRAY ->
                    decodeArray(data, offset, baseHead, (ArrayParameterDefinition) definition);
            case STRUCT ->
                    decodeStruct(data, offset, baseHead, (StructParameterDefinition) definition);
        };
    }

    private DecodeResult decodeAddress(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset + 12, offset + WORD);
        String address = hexlify(slice);
        return new DecodeResult(
                new AddressParameter(definition.isIndexed(), definition.getPosition(), address),
                offset + WORD);
    }

    private DecodeResult decodeUint(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset, offset + WORD);
        BigInteger value = new BigInteger(slice);
        return new DecodeResult(
                new UintParameter(definition.isIndexed(), definition.getPosition(), value),
                offset + WORD);
    }

    private DecodeResult decodeInt(byte[] data, int offset, ParameterDefinition definition) {
        byte[] slice = Arrays.copyOfRange(data, offset, offset + WORD);
        BigInteger value = new BigInteger(slice); // signed
        return new DecodeResult(
                new IntParameter(definition.isIndexed(), definition.getPosition(), value),
                offset + WORD);
    }

    private DecodeResult decodeBool(byte[] data, int offset, ParameterDefinition definition) {
        byte value = data[offset + 31];
        return new DecodeResult(
                new BoolParameter(definition.isIndexed(), definition.getPosition(), value == 1),
                offset + WORD);
    }

    private DecodeResult decodeString(
            byte[] data, int offset, int baseHead, ParameterDefinition def) {
        byte[] rawBytes = this.getRawBytesFromDynamicParam(data, offset, baseHead);
        return new DecodeResult(
                new StringParameter(
                        def.isIndexed(),
                        def.getPosition(),
                        new String(rawBytes, StandardCharsets.UTF_8)),
                offset + WORD);
    }

    private DecodeResult decodeBytes(
            byte[] data, int offset, int baseHead, ParameterDefinition definition) {
        byte[] rawBytes = this.getRawBytesFromDynamicParam(data, offset, baseHead);
        return new DecodeResult(
                new BytesParameter(definition.isIndexed(), definition.getPosition(), rawBytes),
                offset + WORD);
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
                offset + WORD);
    }

    private DecodeResult decodeArray(
            byte[] data, int offset, int baseHead, ArrayParameterDefinition definition) {
        if (definition.isDynamic()) {
            // Array data is in a different data block, so head for that data block changes
            // New head (arrBase) is obtained by adding the relative position of the array plus the
            // current head
            int arrRel =
                    asInt(data, offset); // The relative position is stored in the current offset
            int arrBase = arrRel + baseHead;
            return this.decodeDynamicArray(data, offset, arrBase, definition);
        } else {
            // Array data is directly in current offset
            // Base head does not change
            return this.decodeStaticArray(data, offset, baseHead, definition);
        }
    }

    private DecodeResult decodeDynamicArray(
            byte[] data, int offset, int arrBase, ArrayParameterDefinition definition) {
        List<ContractEventParameter<?>> items = new ArrayList<>();
        ParameterDefinition elementDef = definition.getElementType();

        int length = asInt(data, arrBase);

        int current = (arrBase + WORD);
        for (int i = 0; i < length; i++) {
            DecodeResult elt = decodeParameter(elementDef, data, current, arrBase);
            items.add(elt.parameter());
            current = elt.newOffset();
        }

        return new DecodeResult(
                new ArrayParameter<>(definition.isIndexed(), definition.getPosition(), items),
                offset + WORD);
    }

    private DecodeResult decodeStaticArray(
            byte[] data, int offset, int arrBase, ArrayParameterDefinition definition) {
        List<ContractEventParameter<?>> items = new ArrayList<>();
        ParameterDefinition elementDef = definition.getElementType();

        int length = definition.getFixedLength();
        int current = offset;
        for (int i = 0; i < length; i++) {
            DecodeResult elt = decodeParameter(elementDef, data, current, arrBase);
            items.add(elt.parameter());
            current = elt.newOffset();
        }

        return new DecodeResult(
                new ArrayParameter<>(definition.isIndexed(), definition.getPosition(), items),
                current);
    }

    private DecodeResult decodeStruct(
            byte[] data, int offset, int baseHead, StructParameterDefinition definition) {

        Set<ParameterDefinition> ordered =
                definition.getParameterDefinitions().stream()
                        .sorted(Comparator.comparingInt(ParameterDefinition::getPosition))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        if (definition.isDynamic()) {
            return this.decodeDynamicStruct(data, offset, baseHead, ordered, definition);
        } else {
            return this.decodeStaticStruct(data, offset, baseHead, ordered, definition);
        }
    }

    private DecodeResult decodeStaticStruct(
            byte[] data,
            int offset,
            int baseHead,
            Set<ParameterDefinition> ordered,
            StructParameterDefinition definition) {
        List<ContractEventParameter<?>> values = new ArrayList<>();
        int current = offset;
        for (ParameterDefinition type : ordered) {
            DecodeResult res = decodeParameter(type, data, current, baseHead);
            values.add(res.parameter());
            current = res.newOffset();
        }

        return new DecodeResult(
                new StructParameter(definition.isIndexed(), definition.getPosition(), values),
                current);
    }

    private DecodeResult decodeDynamicStruct(
            byte[] data,
            int offset,
            int baseHead,
            Set<ParameterDefinition> ordered,
            StructParameterDefinition definition) {
        List<ContractEventParameter<?>> values = new ArrayList<>();

        // Struct data is in a different data block, so head for that block changes
        // New head (structBase) is obtained by adding the relative position of the struct plus the
        // current head
        int relStruct =
                asInt(data, offset); // The relative position is stored in the current offset
        int structBase = relStruct + baseHead;

        int current = structBase;
        for (ParameterDefinition type : ordered) {
            DecodeResult res = decodeParameter(type, data, current, structBase);
            values.add(res.parameter());
            current = res.newOffset();
        }

        return new DecodeResult(
                new StructParameter(definition.isIndexed(), definition.getPosition(), values),
                offset + WORD);
    }

    private byte[] getRawBytesFromDynamicParam(byte[] data, int offset, int baseHead) {
        int rel = asInt(data, offset);
        int dynOffset = baseHead + rel;

        int length = asInt(data, dynOffset);
        int start = dynOffset + WORD;
        int end = start + length;

        return Arrays.copyOfRange(data, start, end);
    }
}
