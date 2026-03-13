package io.naryo.infrastructure.store.event.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.node.interactor.block.dto.hedera.BatchKey;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BatchKeyConverter implements AttributeConverter<BatchKey, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BatchKey attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public BatchKey convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, BatchKey.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
