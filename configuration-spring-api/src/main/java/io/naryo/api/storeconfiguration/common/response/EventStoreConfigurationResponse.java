package io.naryo.api.storeconfiguration.common.response;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import lombok.Getter;

@Getter
public abstract class EventStoreConfigurationResponse extends StoreFeatureConfigurationResponse {

    private final EventStoreStrategy strategy;

    protected EventStoreConfigurationResponse(EventStoreStrategy strategy) {
        this.strategy = strategy;
    }
}
