package io.naryo.application.configuration.manager;

public interface ConfigurationManager<T> {

    T load();
}
