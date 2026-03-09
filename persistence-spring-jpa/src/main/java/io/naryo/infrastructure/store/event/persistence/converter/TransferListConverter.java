package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.Transfer;
import jakarta.persistence.Converter;

@Converter
public class TransferListConverter extends BaseJsonConverter<List<Transfer>> {

    @Override
    protected TypeReference<List<Transfer>> getType() {
        return new TypeReference<>() {};
    }
}
