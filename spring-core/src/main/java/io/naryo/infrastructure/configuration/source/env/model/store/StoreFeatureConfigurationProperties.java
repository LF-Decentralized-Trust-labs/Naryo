package io.naryo.infrastructure.configuration.source.env.model.store;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.Getter;

@Getter
public abstract class StoreFeatureConfigurationProperties
        implements StoreFeatureConfigurationDescriptor {

    private final StoreFeatureType type;

    protected StoreFeatureConfigurationProperties(StoreFeatureType type) {
        this.type = type;
    }
}
