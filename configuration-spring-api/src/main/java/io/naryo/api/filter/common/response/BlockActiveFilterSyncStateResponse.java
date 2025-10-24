package io.naryo.api.filter.common.response;

import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import lombok.Getter;

@Getter
public final class BlockActiveFilterSyncStateResponse extends FilterSyncStateResponse {

    private final Long initialBlock;

    private BlockActiveFilterSyncStateResponse(SyncStrategy syncStrategy, Long initialBlock) {
        super(syncStrategy);
        this.initialBlock = initialBlock;
    }

    public static BlockActiveFilterSyncStateResponse fromDomain(
            BlockActiveFilterSyncState blockActiveFilterSyncState) {
        return new BlockActiveFilterSyncStateResponse(
                blockActiveFilterSyncState.getStrategy(),
                blockActiveFilterSyncState.getInitialBlock().value().longValue());
    }
}
