package io.naryo.infrastructure.configuration.persistence.document.store.event;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import lombok.Getter;

@Getter
public abstract class EventStoreConfigurationPropertiesDocument
        extends StoreFeatureConfigurationProperties {

    private final EventStoreStrategy strategy;

    protected EventStoreConfigurationPropertiesDocument(EventStoreStrategy strategy) {
        super(StoreFeatureType.EVENT);
        this.strategy = strategy;
    }
}
