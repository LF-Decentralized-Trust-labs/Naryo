package io.naryo.infrastructure.configuration.persistence.entity.store.event;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreFeatureConfigurationEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public abstract class EventStoreConfigurationEntity extends StoreFeatureConfigurationEntity
        implements EventStoreConfigurationDescriptor {

    protected EventStoreConfigurationEntity() {
        super();
    }
}
