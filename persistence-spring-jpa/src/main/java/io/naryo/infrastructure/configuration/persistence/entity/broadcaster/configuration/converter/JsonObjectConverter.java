package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonObjectConverter implements AttributeConverter<Object, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing object to JSON", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return objectMapper.readValue(dbData, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing JSON to object", e);
        }
    }
}
