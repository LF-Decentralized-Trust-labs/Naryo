package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.NftTransfer;
import jakarta.persistence.Converter;

@Converter
public class NftTransferListConverter extends BaseJsonConverter<List<NftTransfer>> {

    @Override
    protected TypeReference<List<NftTransfer>> getType() {
        return new TypeReference<>() {};
    }
}
