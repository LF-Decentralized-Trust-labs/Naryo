package io.naryo.infrastructure.configuration.persistence.document.store;

import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.store.event.block.BlockStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.event.block.EventStoreTargetPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.filter.FilterStoreConfigurationPropertiesDocument;
import lombok.Getter;

@Getter
public abstract class StoreFeatureConfigurationPropertiesDocument
        implements StoreFeatureConfigurationDescriptor {

    public static StoreFeatureConfigurationPropertiesDocument fromDomain(
            StoreFeatureConfiguration source) {
        return switch (source.getType()) {
            case EVENT:
                {
                    BlockEventStoreConfiguration blockEventStoreConfiguration =
                            (BlockEventStoreConfiguration) source;
                    yield new BlockStoreConfigurationPropertiesDocument(
                            blockEventStoreConfiguration.getTargets().stream()
                                    .map(
                                            e ->
                                                    new EventStoreTargetPropertiesDocument(
                                                            e.type(), e.destination().value()))
                                    .collect(Collectors.toSet()));
                }
            case FILTER_SYNC:
                yield new FilterStoreConfigurationPropertiesDocument(
                        ((FilterStoreConfiguration) source).getDestination().value());
        };
    }
}
