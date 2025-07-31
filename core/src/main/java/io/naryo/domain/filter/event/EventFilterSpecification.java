package io.naryo.domain.filter.event;

import java.util.*;
import java.util.stream.Collectors;

import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.parameter.*;

public record EventFilterSpecification(
        EventName eventName, CorrelationId correlationId, Set<ParameterDefinition> parameters) {

    public EventFilterSpecification(
            EventName eventName, CorrelationId correlationId, Set<ParameterDefinition> parameters) {
        Objects.requireNonNull(eventName, "Event name cannot be null");
        Objects.requireNonNull(parameters, "Parameters cannot be null");
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("Parameters cannot be empty");
        }

        this.eventName = eventName;
        this.correlationId = correlationId;
        this.parameters = parameters.stream().collect(Collectors.toUnmodifiableSet());
    }

    public EventFilterSpecification(String signature, CorrelationId correlationId) {
        this(extractEventName(signature), correlationId, extractParameters(signature));
    }

    public static EventName extractEventName(String signature) {
        Objects.requireNonNull(signature, "Signature cannot be null");
        String name = signature.split("\\(")[0].trim();
        return new EventName(name);
    }

    public static Set<ParameterDefinition> extractParameters(String signature) {
        Objects.requireNonNull(signature, "Signature cannot be null");

        int start = signature.indexOf('(');
        int end = signature.lastIndexOf(')');
        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("Invalid signature format: missing parentheses");
        }

        String paramsString = signature.substring(start + 1, end).trim();
        if (paramsString.isEmpty()) {
            return Set.of();
        }

        List<String> tokens = splitTopLevel(paramsString);
        Set<ParameterDefinition> result = Set.of();

        for (int i = 0; i < tokens.size(); i++) {
            String type = tokens.get(i).trim();
            ParameterDefinition param = parseParameterType(type, i);
            result = addToSet(result, param);
        }

        return result;
    }

    private static Set<ParameterDefinition> addToSet(
            Set<ParameterDefinition> original, ParameterDefinition toAdd) {
        return original.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(
                                        () -> {
                                            var copy = Set.copyOf(original);
                                            return new java.util.LinkedHashSet<>(copy);
                                        }),
                                set -> {
                                    set.add(toAdd);
                                    return Set.copyOf(set);
                                }));
    }

    private static List<String> splitTopLevel(String input) {
        List<String> tokens = new ArrayList<>();
        int level = 0;
        StringBuilder current = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == ',' && level == 0) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                if (c == '(') level++;
                else if (c == ')') level--;
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    private static ParameterDefinition parseParameterType(String type, int position) {
        boolean isIndexed = false;

        if (type.startsWith("indexed ")) {
            isIndexed = true;
            type = type.substring(8).trim();
        } else if (type.contains(" indexed")) {
            isIndexed = true;
            type = type.replace(" indexed", "").trim();
        }

        if (type.endsWith("[]")) {
            String innerType = type.substring(0, type.length() - 2);
            return new ArrayParameterDefinition(position, parseParameterType(innerType, 0), null);
        } else if (type.matches(".+\\[\\d+]$")) {
            int start = type.lastIndexOf('[');
            String innerType = type.substring(0, start);
            int length = Integer.parseInt(type.substring(start + 1, type.length() - 1));
            return new ArrayParameterDefinition(position, parseParameterType(innerType, 0), length);
        } else if (type.startsWith("(") && type.endsWith(")")) {
            String inner = type.substring(1, type.length() - 1);
            Set<ParameterDefinition> innerParams = new HashSet<>();
            List<String> innerTokens = splitTopLevel(inner);
            for (int i = 0; i < innerTokens.size(); i++) {
                innerParams.add(parseParameterType(innerTokens.get(i), i));
            }
            return new StructParameterDefinition(position, innerParams);
        } else if (type.startsWith("uint")) {
            int bits = type.length() > 4 ? Integer.parseInt(type.substring(4)) : 256;
            return new UintParameterDefinition(bits, position, isIndexed);
        } else if (type.startsWith("int")) {
            int bits = type.length() > 3 ? Integer.parseInt(type.substring(3)) : 256;
            return new IntParameterDefinition(bits, position, isIndexed);
        } else if (type.startsWith("bytes") && type.length() > 5) {
            int len = Integer.parseInt(type.substring(5));
            return new BytesFixedParameterDefinition(len, position, isIndexed);
        } else {
            return switch (type) {
                case "bool" -> new BoolParameterDefinition(position, isIndexed);
                case "address" -> new AddressParameterDefinition(position, isIndexed);
                case "string" -> new StringParameterDefinition(position);
                case "bytes" -> new BytesParameterDefinition(position);
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        }
    }

    public String getEventSignature() {
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
