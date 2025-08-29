package io.naryo.infrastructure.configuration.persistence.entity.store.event;

import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreFeatureConfigurationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public abstract class EventStoreConfigurationEntity extends StoreFeatureConfigurationEntity {

    private @Column(name = "strategy", nullable = false) EventStoreStrategy strategy;

    protected EventStoreConfigurationEntity(EventStoreStrategy strategy) {
        super(StoreFeatureType.EVENT);
        this.strategy = strategy;
    }
}
