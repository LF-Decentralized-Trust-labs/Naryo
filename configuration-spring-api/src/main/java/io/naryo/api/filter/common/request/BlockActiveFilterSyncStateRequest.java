package io.naryo.api.filter.common.request;

import java.math.BigInteger;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BlockActiveFilterSyncStateRequest extends FilterSyncStateRequest {

    private final @NotNull Long initialBlock;

    public BlockActiveFilterSyncStateRequest(Long initialBlock) {
        super(SyncStrategy.BLOCK_BASED);
        this.initialBlock = initialBlock;
    }

    @Override
    public FilterSyncState toDomain() {
        return new BlockActiveFilterSyncState(
                new NonNegativeBlockNumber(BigInteger.valueOf(initialBlock)));
    }
}
