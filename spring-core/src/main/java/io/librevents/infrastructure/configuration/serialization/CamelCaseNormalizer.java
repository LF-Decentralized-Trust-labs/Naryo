package io.librevents.infrastructure.configuration.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CamelCaseNormalizer {

    /**
     * Normalizes the keys of a map to camel case.
     *
     * @param originalMap the original map with keys to be normalized
     * @return a new map with keys in camel case
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> normalize(Map<String, Object> originalMap) {
        Map<String, Object> normalizedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String normalizedKey = toCamelCase(entry.getKey());
            Object value = entry.getValue();

            if (value instanceof Map) {
                value = normalize((Map<String, Object>) value);
            }

            normalizedMap.put(normalizedKey, value);
        }

        return normalizedMap;
    }

    public static String toCamelCase(String input) {
        if (!input.contains("_") && !input.contains("-")) {
            return input;
        }

        String[] parts = input.split("[-_]");
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == 0) {
                camelCaseString.append(part.toLowerCase());
            } else if (!part.isEmpty()) {
                camelCaseString
                        .append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }
}
