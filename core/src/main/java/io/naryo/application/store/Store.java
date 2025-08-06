package io.naryo.application.store;

import java.util.Optional;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;

public interface Store<C extends ActiveStoreConfiguration, K, D> {

    void save(C configuration, K key, D data);

    Optional<D> get(C configuration, K key);

    boolean supports(StoreType type, Class<?> clazz);
}
