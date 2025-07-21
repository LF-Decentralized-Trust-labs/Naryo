package io.naryo.infrastructure.configuration.source.env.model.filter.event.sync;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class FilterSyncProperties implements FilterSyncDescriptor {

    private final @NotNull SyncStrategy strategy;

    protected FilterSyncProperties(SyncStrategy strategy) {
        this.strategy = strategy;
    }
}
