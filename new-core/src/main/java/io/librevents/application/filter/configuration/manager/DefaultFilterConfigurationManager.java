package io.librevents.application.filter.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import io.librevents.application.configuration.manager.BaseCollectionConfigurationManager;
import io.librevents.application.configuration.provider.CollectionConfigurationProvider;
import io.librevents.domain.filter.Filter;

import static java.util.stream.Collectors.toMap;

public final class DefaultFilterConfigurationManager
        extends BaseCollectionConfigurationManager<Filter, UUID>
        implements FilterConfigurationManager {

    DefaultFilterConfigurationManager(
            List<CollectionConfigurationProvider<Filter>> collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<Filter, ?, Map<UUID, Filter>> getCollector() {
        return toMap(
                Filter::getId,
                Function.identity(),
                (oldFilter, newFilter) -> newFilter,
                LinkedHashMap::new);
    }
}
