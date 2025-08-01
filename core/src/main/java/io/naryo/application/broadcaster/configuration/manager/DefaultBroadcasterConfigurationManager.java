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
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.broadcaster.target.*;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;
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
                BroadcasterDescriptor::getId,
                Function.identity(),
                BroadcasterDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected Broadcaster map(BroadcasterDescriptor source) {
        UUID id = source.getId();
        UUID configurationId = valueOrNull(source.getConfigurationId());
        BroadcasterTarget target = buildTarget(valueOrNull(source.getTarget()));
        return new Broadcaster(id, target, configurationId);
    }

    private BroadcasterTarget buildTarget(BroadcasterTargetDescriptor descriptor) {
        Destination destination = new Destination(valueOrNull(descriptor.getDestination()));
        return switch (descriptor.getType()) {
            case ALL -> new AllBroadcasterTarget(destination);
            case BLOCK -> new BlockBroadcasterTarget(destination);
            case TRANSACTION -> new TransactionBroadcasterTarget(destination);
            case CONTRACT_EVENT -> new ContractEventBroadcasterTarget(destination);
            case FILTER -> {
                var filterDescriptor = (FilterBroadcasterTargetDescriptor) descriptor;
                UUID filterId = filterDescriptor.getFilterId();
                yield new FilterEventBroadcasterTarget(destination, filterId);
            }
        };
    }
}
