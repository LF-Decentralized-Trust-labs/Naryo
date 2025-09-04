package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "store_features")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "feature")
@Getter
public abstract class StoreFeatureConfigurationEntity
        implements StoreFeatureConfigurationDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(
            name = "id",
            nullable = false,
            updatable = false) UUID id;
}
