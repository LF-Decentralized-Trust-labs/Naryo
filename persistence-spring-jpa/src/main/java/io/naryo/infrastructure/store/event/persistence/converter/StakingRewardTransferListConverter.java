package io.naryo.infrastructure.store.event.persistence.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.naryo.application.node.interactor.block.dto.hedera.StakingRewardTransfer;
import jakarta.persistence.Converter;

@Converter
public class StakingRewardTransferListConverter
        extends BaseJsonConverter<List<StakingRewardTransfer>> {

    @Override
    protected TypeReference<List<StakingRewardTransfer>> getType() {
        return new TypeReference<>() {};
    }
}
