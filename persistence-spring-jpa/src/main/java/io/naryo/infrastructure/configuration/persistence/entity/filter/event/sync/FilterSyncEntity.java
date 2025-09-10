package io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync;

import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_sync")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@Getter
public abstract class FilterSyncEntity implements FilterSyncDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id") UUID id;
}
