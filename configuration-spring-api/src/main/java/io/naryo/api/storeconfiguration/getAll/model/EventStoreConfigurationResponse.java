package io.naryo.api.storeconfiguration.getAll.model;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Base event store configuration")
@Getter
public abstract class EventStoreConfigurationResponse extends StoreFeatureConfigurationResponse {

    private final EventStoreStrategy strategy;

    protected EventStoreConfigurationResponse(EventStoreStrategy strategy) {
        this.strategy = strategy;
    }
}
