package io.naryo.infrastructure.configuration.source.env.model.filter.event.sync;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class FilterSyncProperties implements FilterSyncDescriptor {

    private final @Getter @NotNull SyncType type;

    public FilterSyncProperties(SyncType type) {
        this.type = type;
    }
}
