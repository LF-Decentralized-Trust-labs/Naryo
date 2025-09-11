package io.naryo.infrastructure.store;

import io.naryo.application.store.Store;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;

import static io.naryo.domain.JpaConstants.JPA_TYPE;

public abstract class JpaStore<K, D> implements Store<JpaActiveStoreConfiguration, K, D> {

    protected abstract Class<?> getTargetDataClass();

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equals(JPA_TYPE) && clazz.isAssignableFrom(getTargetDataClass());
    }
}
