package io.naryo.infrastructure.configuration.persistence.document.store;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.Getter;

@Getter
public abstract class StoreFeatureConfigurationPropertiesDocument
        implements StoreFeatureConfigurationDescriptor {

    private final StoreFeatureType type;

    protected StoreFeatureConfigurationPropertiesDocument(StoreFeatureType type) {
        this.type = type;
    }
}
