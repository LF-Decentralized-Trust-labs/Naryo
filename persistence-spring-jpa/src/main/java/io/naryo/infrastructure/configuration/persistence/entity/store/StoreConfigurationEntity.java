package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_configuration")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "state")
@NoArgsConstructor
@Setter
public abstract class StoreConfigurationEntity implements StoreConfigurationDescriptor {

    private @Column(name = "node_id") @Id UUID nodeId;

    public StoreConfigurationEntity(UUID nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getNodeId() {
        return this.nodeId;
    }

    public static StoreConfigurationEntity fromDomain(StoreConfiguration source) {
        return switch (source) {
            case ActiveStoreConfiguration active -> {
                List<StoreFeatureConfigurationEntity> storeFeatureConfigurationEntities =
                        active.getFeatures().values().stream()
                                .map(StoreFeatureConfigurationEntity::fromDomain)
                                .toList();

                yield new ActiveStoreConfigurationEntity(
                        active.getNodeId(),
                        active.getType().getName(),
                        storeFeatureConfigurationEntities,
                        new HashMap<>());
            }
            case InactiveStoreConfiguration ignored ->
                    new InactiveStoreConfigurationEntity(source.getNodeId());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported store type: " + source.getClass());
        };
    }
}
