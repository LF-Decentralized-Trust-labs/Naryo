package io.naryo.infrastructure.configuration.source.env.model.store.event;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import lombok.Getter;

@Getter
public abstract class EventStoreConfigurationProperties extends StoreFeatureConfigurationProperties
        implements EventStoreConfigurationDescriptor {

    private final EventStoreStrategy strategy;

    protected EventStoreConfigurationProperties(EventStoreStrategy strategy) {
        super(StoreFeatureType.EVENT);
        this.strategy = strategy;
    }
}
