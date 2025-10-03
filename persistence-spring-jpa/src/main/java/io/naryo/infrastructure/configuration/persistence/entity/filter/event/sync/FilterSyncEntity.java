package io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync;

import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.sync.NoFilterSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_filter_sync")
@DiscriminatorColumn(name = "sync_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@Getter
public abstract class FilterSyncEntity implements FilterSyncDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id") UUID id;

    public static FilterSyncEntity fromDomain(FilterSyncState source) {
        return switch (source) {
            case BlockActiveFilterSyncState block ->
                    new BlockFilterSyncEntity(block.getInitialBlock().value());
            case NoFilterSyncState ignore -> null;
            default ->
                    throw new IllegalStateException(
                            "Unsupported sync state: " + source.getClass().getSimpleName());
        };
    }
}
