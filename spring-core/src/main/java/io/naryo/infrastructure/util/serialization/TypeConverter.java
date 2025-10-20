package io.naryo.infrastructure.util.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TypeConverter {
    private static final ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private TypeConverter() {}

    public static <T> T castToType(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        return mapper.convertValue(value, targetType);
    }
}
