package io.librevents.application.broadcaster.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import io.librevents.application.configuration.manager.BaseCollectionConfigurationManager;
import io.librevents.application.configuration.provider.CollectionConfigurationProvider;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;

import static java.util.stream.Collectors.toMap;

public final class DefaultBroadcasterConfigurationConfigurationManager
        extends BaseCollectionConfigurationManager<BroadcasterConfiguration, UUID>
        implements BroadcasterConfigurationConfigurationManager {

    public DefaultBroadcasterConfigurationConfigurationManager(
            List<? extends CollectionConfigurationProvider<BroadcasterConfiguration>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<BroadcasterConfiguration, ?, Map<UUID, BroadcasterConfiguration>>
            getCollector() {
        return toMap(
                BroadcasterConfiguration::getId,
                Function.identity(),
                BroadcasterConfiguration::merge,
                LinkedHashMap::new);
    }
}
