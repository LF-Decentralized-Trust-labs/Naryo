package io.naryo.infrastructure.store.event;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.event.Event;

import static io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration.JPA_STORE_TYPE;

public abstract class JpaEventStore<K, D extends Event<?>>
        implements EventStore<JpaActiveStoreConfiguration, K, D> {

    protected abstract Class<?> getTargetDataClass();

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equals(JPA_STORE_TYPE)
                && clazz.isAssignableFrom(getTargetDataClass());
    }
}
