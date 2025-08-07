package io.naryo.application.configuration.source.model.store.event;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;

public interface EventStoreConfigurationDescriptor extends StoreFeatureConfigurationDescriptor {

    EventStoreStrategy getStrategy();

    @Override
    default StoreFeatureType getType() {
        return StoreFeatureType.EVENT;
    }
}
