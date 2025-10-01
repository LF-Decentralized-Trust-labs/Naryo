package io.naryo.infrastructure.configuration.persistence.document.filter.event.sync;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.sync.NoFilterSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class FilterSyncDocument implements FilterSyncDescriptor {
    public static FilterSyncDocument fromDomain(FilterSyncState source) {
        return switch (source) {
            case BlockActiveFilterSyncState block ->
                    new BlockFilterSyncDocument(block.getInitialBlock().value());
            case NoFilterSyncState ignore -> null;
            default ->
                    throw new IllegalStateException(
                            "Unsupported sync state: " + source.getClass().getSimpleName());
        };
    }
}
