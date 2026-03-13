package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.node.interactor.block.dto.hedera.TokenTransfer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TokenTransferListConverter implements AttributeConverter<List<TokenTransfer>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<TokenTransfer> attribute) {
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
    public List<TokenTransfer> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
