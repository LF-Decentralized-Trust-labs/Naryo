package io.naryo.infrastructure.configuration.persistence.document.store.event;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreFeatureConfigurationPropertiesDocument;
import lombok.Getter;

@Getter
public abstract class EventStoreConfigurationPropertiesDocument
        extends StoreFeatureConfigurationPropertiesDocument {

    private final EventStoreStrategy strategy;

    protected EventStoreConfigurationPropertiesDocument(EventStoreStrategy strategy) {
        super(StoreFeatureType.EVENT);
        this.strategy = strategy;
    }
}
