package io.naryo.infrastructure.event.mongo.event;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.event.Event;

import static io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration.MONGO_STORE_TYPE;

public abstract class MongoEventStore<K, D extends Event>
        implements EventStore<MongoStoreConfiguration, K, D> {

    protected abstract Class<?> getTargetDataClass();

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equalsIgnoreCase(MONGO_STORE_TYPE)
                && clazz.isAssignableFrom(this.getTargetDataClass());
    }
}
