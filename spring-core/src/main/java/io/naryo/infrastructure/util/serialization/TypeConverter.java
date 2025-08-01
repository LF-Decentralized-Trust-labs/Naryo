package io.naryo.infrastructure.util.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class TypeConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    private TypeConverter() {}

    public static <T> T castToType(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        return mapper.convertValue(value, targetType);
    }
}
