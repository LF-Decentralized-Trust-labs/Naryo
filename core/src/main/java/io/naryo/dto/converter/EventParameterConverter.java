package io.naryo.dto.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.dto.event.parameter.EventParameter;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventParameterConverter implements AttributeConverter<EventParameter, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(EventParameter attribute) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising EventParameters", e);
        }
    }

    @Override
    public EventParameter convertToEntityAttribute(byte[] dbData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(dbData, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error serialising EventParameters", e);
        }
    }
}
