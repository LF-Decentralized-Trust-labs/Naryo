package io.naryo.application.broadcaster.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.broadcaster.target.*;

import static java.util.stream.Collectors.toMap;

public final class DefaultBroadcasterConfigurationManager
        extends BaseCollectionConfigurationManager<Broadcaster, BroadcasterDescriptor, UUID>
        implements BroadcasterConfigurationManager {

    public DefaultBroadcasterConfigurationManager(
            List<? extends CollectionSourceProvider<BroadcasterDescriptor>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<BroadcasterDescriptor, ?, Map<UUID, BroadcasterDescriptor>> getCollector() {
        return toMap(
                BroadcasterDescriptor::id,
                Function.identity(),
                BroadcasterDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected Broadcaster map(BroadcasterDescriptor source) {
        Destination destination = new Destination(source.target().getDestination());
        return new Broadcaster(
                source.id(),
                switch (source.target().getType()) {
                    case ALL -> new AllBroadcasterTarget(destination);
                    case BLOCK -> new BlockBroadcasterTarget(destination);
                    case FILTER ->
                            new FilterEventBroadcasterTarget(
                                    destination,
                                    ((FilterBroadcasterTargetDescriptor) source.target())
                                            .getFilterId());
                    case TRANSACTION -> new TransactionBroadcasterTarget(destination);
                    case CONTRACT_EVENT -> new ContractEventBroadcasterTarget(destination);
                },
                source.configurationId());
    }
}
