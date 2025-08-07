package io.naryo.application.configuration.source.model.store;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;

public interface StoreFeatureConfigurationDescriptor
        extends MergeableDescriptor<StoreFeatureConfigurationDescriptor> {

    StoreFeatureType getType();
}
