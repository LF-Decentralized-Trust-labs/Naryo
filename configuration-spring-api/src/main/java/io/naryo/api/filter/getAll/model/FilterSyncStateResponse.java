package io.naryo.api.filter.getAll.model;

import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import lombok.Getter;

@Getter
public abstract class FilterSyncStateResponse {

    private final SyncStrategy strategy;

    public FilterSyncStateResponse(SyncStrategy strategy) {
        this.strategy = strategy;
    }

    protected static FilterSyncStateResponse fromDomain(FilterSyncState filterSyncState) {
        return switch (filterSyncState) {
            case BlockActiveFilterSyncState block ->
                    BlockActiveFilterSyncStateResponse.fromDomain(block);
            default -> throw new IllegalStateException("Unexpected value: " + filterSyncState);
        };
    }
}
