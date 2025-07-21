package io.naryo.application.configuration.manager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.Descriptor;

public abstract class BaseCollectionConfigurationManager<T, S extends Descriptor, K>
        implements CollectionConfigurationManager<T> {

    protected final List<? extends CollectionSourceProvider<S>> providers;

    protected BaseCollectionConfigurationManager(
            List<? extends CollectionSourceProvider<S>> providers) {
        this.providers = providers;
    }

    @Override
    public Collection<T> load() {
        return providers.stream()
                .sorted((p1, p2) -> Integer.compare(p2.priority(), p1.priority()))
                .flatMap(provider -> provider.load().stream())
                .collect(
                        Collectors.collectingAndThen(
                                getCollector(),
                                map -> map.values().stream().map(this::map).toList()));
    }

    protected abstract Collector<S, ?, Map<K, S>> getCollector();

    protected abstract T map(S source);
}
