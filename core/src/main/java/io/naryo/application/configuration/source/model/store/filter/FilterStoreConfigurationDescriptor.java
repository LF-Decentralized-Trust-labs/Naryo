package io.naryo.application.configuration.source.model.store.filter;

import java.util.Optional;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface FilterStoreConfigurationDescriptor extends StoreFeatureConfigurationDescriptor {

    Optional<String> getDestination();

    void setDestination(String destination);

    @Override
    default StoreFeatureType getType() {
        return StoreFeatureType.FILTER_SYNC;
    }

    @Override
    default StoreFeatureConfigurationDescriptor merge(StoreFeatureConfigurationDescriptor other) {
        if (!(other instanceof FilterStoreConfigurationDescriptor otherFilterStoreConfiguration)) {
            return this;
        }
        mergeOptionals(
                this::setDestination,
                this.getDestination(),
                otherFilterStoreConfiguration.getDestination());
        return this;
    }
}
