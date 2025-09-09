package io.naryo.application.configuration.source.mapper;

import java.util.Map;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;

public abstract class StoreConfigurationDescriptorMapper {

    public static Map<StoreFeatureType, StoreFeatureConfiguration> map(
            ActiveStoreConfigurationDescriptor descriptor) {
        return descriptor.getFeatures().entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                value -> configurationDescriptorToConfiguration(value.getValue())));
    }

    private static StoreFeatureConfiguration configurationDescriptorToConfiguration(
            StoreFeatureConfigurationDescriptor descriptor) {
        return switch (descriptor) {
            case BlockEventStoreConfigurationDescriptor blockEventStoreConfigurationDescriptor ->
                    blockEventStoreDescriptorToEventStore(blockEventStoreConfigurationDescriptor);
            case FilterStoreConfigurationDescriptor filterStoreConfigurationDescriptor ->
                    filterStoreDescriptorToFilterStore(filterStoreConfigurationDescriptor);
            default -> throw new IllegalStateException("Unexpected value: " + descriptor.getType());
        };
    }

    private static EventStoreConfiguration blockEventStoreDescriptorToEventStore(
            BlockEventStoreConfigurationDescriptor descriptor) {
        return new BlockEventStoreConfiguration(
                descriptor.getTargets().stream()
                        .map(
                                target ->
                                        new EventStoreTarget(
                                                target.type(),
                                                new Destination(target.destination())))
                        .collect(Collectors.toSet()));
    }

    private static FilterStoreConfiguration filterStoreDescriptorToFilterStore(
            FilterStoreConfigurationDescriptor filter) {
        return new FilterStoreConfiguration(
                filter.getDestination().isPresent()
                        ? new Destination(filter.getDestination().get())
                        : null);
    }
}
