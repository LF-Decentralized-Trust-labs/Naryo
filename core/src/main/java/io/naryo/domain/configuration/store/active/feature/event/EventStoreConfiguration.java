package io.naryo.domain.configuration.store.active.feature.event;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EventStoreConfiguration extends StoreFeatureConfiguration {

    private final EventStoreStrategy strategy;

    public EventStoreConfiguration(EventStoreStrategy strategy) {
        super(StoreFeatureType.EVENT);
        this.strategy = strategy;
    }
}
