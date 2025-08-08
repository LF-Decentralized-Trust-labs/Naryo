package io.naryo.application.store;

import java.util.List;
import java.util.Optional;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;

public interface Store<C extends ActiveStoreConfiguration, K, D> {

    void save(C configuration, K key, D data);

    Optional<D> get(C configuration, K key);

    List<D> get(C configuration, List<K> keys);

    boolean supports(StoreType type, Class<?> clazz);
}
