package io.naryo.application.configuration.source.model.store;

import java.util.Map;

import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;

import static io.naryo.application.common.util.MergeUtil.*;

public interface ActiveStoreConfigurationDescriptor extends StoreConfigurationDescriptor {

    StoreType getType();

    Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> getFeatures();

    Map<String, Object> getAdditionalProperties();

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setFeatures(Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features);

    @Override
    default StoreState getState() {
        return StoreState.ACTIVE;
    }

    @Override
    default StoreConfigurationDescriptor merge(StoreConfigurationDescriptor other) {
        if (!(other
                instanceof ActiveStoreConfigurationDescriptor otherActiveEventStoreConfiguration)) {
            return this;
        }

        mergeMaps(
                this::setFeatures,
                this.getFeatures(),
                otherActiveEventStoreConfiguration.getFeatures());
        mergeMaps(
                this::setAdditionalProperties,
                this.getAdditionalProperties(),
                otherActiveEventStoreConfiguration.getAdditionalProperties());

        return StoreConfigurationDescriptor.super.merge(otherActiveEventStoreConfiguration);
    }
}
