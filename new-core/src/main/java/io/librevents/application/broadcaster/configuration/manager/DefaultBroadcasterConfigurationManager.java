package io.librevents.application.broadcaster.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import io.librevents.application.configuration.manager.BaseCollectionConfigurationManager;
import io.librevents.application.configuration.provider.CollectionConfigurationProvider;
import io.librevents.domain.broadcaster.Broadcaster;

import static java.util.stream.Collectors.toMap;

public final class DefaultBroadcasterConfigurationManager
        extends BaseCollectionConfigurationManager<Broadcaster, UUID>
        implements BroadcasterConfigurationManager {

    public DefaultBroadcasterConfigurationManager(
            List<? extends CollectionConfigurationProvider<Broadcaster>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<Broadcaster, ?, Map<UUID, Broadcaster>> getCollector() {
        return toMap(
                Broadcaster::getId, Function.identity(), Broadcaster::merge, LinkedHashMap::new);
    }
}
