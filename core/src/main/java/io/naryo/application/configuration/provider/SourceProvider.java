package io.naryo.application.configuration.provider;

public interface SourceProvider<T> {

    T load();

    int priority();
}
