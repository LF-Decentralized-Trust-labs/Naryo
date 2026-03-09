package io.naryo.infrastructure.store.event.persistence.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.BatchKey;
import jakarta.persistence.Converter;

@Converter
public class BatchKeyConverter extends BaseJsonConverter<BatchKey> {
    @Override
    protected TypeReference<BatchKey> getType() {
        return new TypeReference<>() {};
    }
}
