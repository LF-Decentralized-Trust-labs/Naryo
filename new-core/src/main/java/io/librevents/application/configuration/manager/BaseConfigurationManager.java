package io.librevents.application.configuration.manager;

import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

import io.librevents.application.configuration.provider.ConfigurationProvider;

public abstract class BaseConfigurationManager<T> implements ConfigurationManager<T> {

    protected final List<ConfigurationProvider<T>> providers;

    protected BaseConfigurationManager(List<ConfigurationProvider<T>> providers) {
        this.providers = providers;
    }

    @Override
    public T load() {
        return providers.stream()
                .sorted(Comparator.comparingInt(ConfigurationProvider::priority))
                .map(ConfigurationProvider::load)
                .reduce(mergeFunction())
                .orElseThrow(() -> new IllegalStateException("No configuration available"));
    }

    protected abstract BinaryOperator<T> mergeFunction();
}
