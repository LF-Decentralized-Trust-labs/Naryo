package io.naryo.application.store.filter;

import java.util.UUID;

import io.naryo.application.store.Store;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;

public interface FilterStore<C extends ActiveStoreConfiguration>
        extends Store<C, UUID, FilterState> {}
