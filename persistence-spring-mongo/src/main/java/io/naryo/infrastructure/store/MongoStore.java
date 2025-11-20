package io.naryo.infrastructure.store;

import io.naryo.application.store.Store;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;

import static io.naryo.domain.MongoConstants.MONGO_TYPE;

public abstract class MongoStore<K, D> implements Store<MongoStoreConfiguration, K, D> {

    protected abstract Class<?> getTargetDataClass();

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equalsIgnoreCase(MONGO_TYPE) && clazz == this.getTargetDataClass();
    }
}
