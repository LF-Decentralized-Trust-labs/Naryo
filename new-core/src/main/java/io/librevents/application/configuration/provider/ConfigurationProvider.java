package io.librevents.application.configuration.provider;

public interface ConfigurationProvider<T> {

    T load();

    int priority();
}
