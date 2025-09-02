package io.naryo.infrastructure.configuration.persistence.entity.store.event;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreFeatureConfigurationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public abstract class EventStoreConfigurationEntity extends StoreFeatureConfigurationEntity
        implements EventStoreConfigurationDescriptor {

    protected EventStoreConfigurationEntity() {
        super();
    }
}
