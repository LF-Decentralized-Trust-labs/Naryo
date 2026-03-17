package io.naryo.infrastructure.store.event.persistence.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import jakarta.persistence.Converter;

@Converter
public class TransactionNameConverter extends BaseJsonConverter<TransactionName> {
    @Override
    protected TypeReference<TransactionName> getType() {
        return new TypeReference<>() {};
    }
}
