package io.naryo.api.filter.getAll.model;

import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Block active filter sync state")
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
