package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
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
            case ActiveStoreConfiguration active ->
                    new ActiveStoreConfigurationEntity(
                            active.getNodeId(),
                            active.getType().getName(),
                            StoreFeatureConfigurationEntity.flatFeaturesMap(
                                    active.getFeatures().entrySet().stream()
                                            .collect(
                                                    Collectors.toMap(
                                                            Map.Entry::getKey,
                                                            e ->
                                                                    StoreFeatureConfigurationEntity
                                                                            .fromDomain(
                                                                                    e
                                                                                            .getValue())))),
                            new HashMap<>());
            case InactiveStoreConfiguration inactive -> new InactiveStoreConfigurationEntity();
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported store type: " + source.getClass());
        };
    }

    public static StoreConfigurationEntity fromDescriptor(StoreConfigurationDescriptor descriptor) {
        return switch (descriptor) {
            case ActiveStoreConfigurationDescriptor active ->
                    new ActiveStoreConfigurationEntity(
                            active.getNodeId(),
                            active.getType().getName(),
                            StoreFeatureConfigurationEntity.flatFeaturesMap(active.getFeatures()),
                            active.getAdditionalProperties());
            case InactiveStoreConfigurationDescriptor inactive ->
                    new InactiveStoreConfigurationEntity();
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported store type: " + descriptor.getClass());
        };
    }
}
