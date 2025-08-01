package io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync;

import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sync_type")
@NoArgsConstructor
@Getter
public abstract class FilterSyncEntity implements FilterSyncDescriptor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
