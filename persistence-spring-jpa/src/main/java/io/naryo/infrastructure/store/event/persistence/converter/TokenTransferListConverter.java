package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.TokenTransfer;
import jakarta.persistence.Converter;

@Converter
public class TokenTransferListConverter extends BaseJsonConverter<List<TokenTransfer>> {

    @Override
    protected TypeReference<List<TokenTransfer>> getType() {
        return new TypeReference<>() {};
    }
}
