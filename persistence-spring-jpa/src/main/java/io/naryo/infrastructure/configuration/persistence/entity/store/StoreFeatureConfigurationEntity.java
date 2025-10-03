package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.store.event.BlockStoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.entity.store.event.EventStoreTargetEntity;
import io.naryo.infrastructure.configuration.persistence.entity.store.filter.FilterStoreConfigurationEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "store_feature")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "feature")
@Getter
public abstract class StoreFeatureConfigurationEntity
        implements StoreFeatureConfigurationDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(
            name = "id",
            nullable = false,
            updatable = false) UUID id;

    public static List<StoreFeatureConfigurationEntity> flatFeaturesMap(
            Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features) {
        if (features.values().stream()
                .allMatch(f -> f instanceof StoreFeatureConfigurationEntity)) {
            return (List<StoreFeatureConfigurationEntity>) features.values();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported feature type for JPA entity: " + features.getClass());
        }
    }

    public static StoreFeatureConfigurationEntity fromDomain(StoreFeatureConfiguration source) {
        return switch (source.getType()) {
            case EVENT:
                {
                    BlockEventStoreConfiguration blockEventStoreConfiguration =
                            (BlockEventStoreConfiguration) source;
                    yield new BlockStoreConfigurationEntity(
                            blockEventStoreConfiguration.getTargets().stream()
                                    .map(
                                            e ->
                                                    new EventStoreTargetEntity(
                                                            e.type(), e.destination().value()))
                                    .collect(Collectors.toSet()));
                }
            case FILTER_SYNC:
                yield new FilterStoreConfigurationEntity(
                        ((FilterStoreConfiguration) source).getDestination().value());
        };
    }
}
