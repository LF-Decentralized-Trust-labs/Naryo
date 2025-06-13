package io.naryo.application.configuration.provider;

public interface ConfigurationProvider<T> {

    T load();

    int priority();
}
