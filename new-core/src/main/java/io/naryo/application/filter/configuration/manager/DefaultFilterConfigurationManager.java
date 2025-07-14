package io.naryo.application.filter.configuration.manager;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.filter.configuration.provider.FilterConfigurationProvider;
import io.naryo.domain.filter.Filter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;

public final class DefaultFilterConfigurationManager
        extends BaseCollectionConfigurationManager<Filter, UUID>
        implements FilterConfigurationManager {

    public DefaultFilterConfigurationManager(
            List<FilterConfigurationProvider> collectionConfigurationProviders) {
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
