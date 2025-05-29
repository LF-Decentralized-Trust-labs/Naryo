package io.librevents.application.configuration.manager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import io.librevents.application.configuration.provider.CollectionConfigurationProvider;
import io.librevents.application.configuration.provider.ConfigurationProvider;

public abstract class BaseCollectionConfigurationManager<T, K>
        implements CollectionConfigurationManager<T> {

    protected final List<? extends CollectionConfigurationProvider<T>> providers;

    protected BaseCollectionConfigurationManager(
            List<? extends CollectionConfigurationProvider<T>> providers) {
        this.providers = providers;
    }

    @Override
    public Collection<T> load() {
        return providers.stream()
                .sorted(Comparator.comparingInt(ConfigurationProvider::priority))
                .flatMap(provider -> provider.load().stream())
                .collect(getCollector())
                .values();
    }

    protected abstract Collector<T, ?, Map<K, T>> getCollector();
}
