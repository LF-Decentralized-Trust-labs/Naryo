package io.naryo.application.configuration.manager;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.provider.SourceProvider;
import io.naryo.application.configuration.source.model.Descriptor;

public abstract class BaseConfigurationManager<T, S extends Descriptor>
        implements ConfigurationManager<T> {

    protected final List<? extends SourceProvider<S>> providers;

    protected BaseConfigurationManager(List<? extends SourceProvider<S>> providers) {
        this.providers = providers;
    }

    @Override
    public T load() {
        return providers.stream()
                .sorted(Comparator.comparingInt(SourceProvider::priority))
                .map(SourceProvider::load)
                .filter(Objects::nonNull)
                .reduce(mergeFunction())
                .map(this::map)
                .orElseThrow(() -> new IllegalStateException("No configuration available"));
    }

    protected abstract BinaryOperator<S> mergeFunction();

    protected abstract T map(S source);
}
