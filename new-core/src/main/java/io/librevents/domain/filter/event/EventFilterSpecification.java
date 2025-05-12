package io.librevents.domain.filter.event;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.librevents.domain.common.event.EventName;
import io.librevents.domain.filter.event.parameter.*;

public record EventFilterSpecification(
        EventName eventName, CorrelationId correlationId, Set<ParameterDefinition> parameters) {

    public EventFilterSpecification {
        Objects.requireNonNull(eventName, "Event name cannot be null");
        Objects.requireNonNull(parameters, "Parameters cannot be null");
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("Parameters cannot be empty");
        }
    }

    public String getEventSignature() {
        // Sort parameters by position
        return eventName.value()
                + parameters.stream()
                        .sorted(Comparator.comparingInt(ParameterDefinition::getPosition))
                        .map(this::buildTypeString)
                        .collect(Collectors.joining(",", "(", ")"));
    }

    private String buildTypeString(ParameterDefinition param) {
        return switch (param.getType()) {
            case ADDRESS -> "address";
            case BOOL -> "bool";
            case STRING -> "string";
            case BYTES -> "bytes";
            case UINT -> {
                if (param instanceof UintParameterDefinition uintParam)
                    yield "uint" + uintParam.getBitSize();
                throw new IllegalArgumentException("UINT parameter missing bitSize");
            }
            case INT -> {
                if (param instanceof IntParameterDefinition intParam)
                    yield "int" + intParam.getBitSize();
                throw new IllegalArgumentException("INT parameter missing bitSize");
            }
            case BYTES_FIXED -> {
                if (param instanceof BytesFixedParameterDefinition bytesParam)
                    yield "bytes" + bytesParam.getByteLength();
                throw new IllegalArgumentException("BYTES_FIXED missing length");
            }
            case STRUCT -> {
                if (param instanceof StructParameterDefinition structParam) {
                    var inner =
                            structParam.getParameterDefinitions().stream()
                                    .sorted(
                                            Comparator.comparingInt(
                                                    ParameterDefinition::getPosition))
                                    .map(this::buildTypeString)
                                    .collect(Collectors.joining(","));
                    yield "(" + inner + ")";
                }
                throw new IllegalArgumentException("STRUCT parameter missing definition list");
            }
            case ARRAY -> {
                if (param instanceof ArrayParameterDefinition arrayParam) {
                    var elementType = buildTypeString(arrayParam.getElementType());
                    yield arrayParam.getFixedLength() != null
                            ? elementType + "[" + arrayParam.getFixedLength() + "]"
                            : elementType + "[]";
                }
                throw new IllegalArgumentException("ARRAY parameter missing elementType");
            }
        };
    }
}
