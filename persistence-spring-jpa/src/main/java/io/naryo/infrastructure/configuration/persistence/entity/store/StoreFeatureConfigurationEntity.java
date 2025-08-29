package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_features")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "feature")
@Getter
@NoArgsConstructor
public abstract class StoreFeatureConfigurationEntity
        implements StoreFeatureConfigurationDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(
            name = "id",
            nullable = false,
            updatable = false) UUID id;

    private @Enumerated(EnumType.STRING) @Column(name = "feature_type", nullable = false)
    StoreFeatureType type;

    protected StoreFeatureConfigurationEntity(StoreFeatureType type) {
        this.type = type;
    }
}
