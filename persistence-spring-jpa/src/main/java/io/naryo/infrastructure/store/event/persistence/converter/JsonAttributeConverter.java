package io.naryo.infrastructure.store.event.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class JsonAttributeConverter<T> implements AttributeConverter<T, String> {
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    @Override
    public String convertToDatabaseColumn(T attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert attribute to json", e);
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert json to attribute", e);
        }
    }
}
