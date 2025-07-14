package io.naryo.application.filter.configuration.manager;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.filter.transaction.TransactionFilter;

import static java.util.stream.Collectors.toMap;

public final class DefaultFilterConfigurationManager
        extends BaseCollectionConfigurationManager<Filter, FilterDescriptor, UUID>
        implements FilterConfigurationManager {

    public DefaultFilterConfigurationManager(
            List<? extends CollectionSourceProvider<FilterDescriptor>>
                    collectionConfigurationProviders) {
        super(collectionConfigurationProviders);
    }

    @Override
    protected Collector<FilterDescriptor, ?, Map<UUID, FilterDescriptor>> getCollector() {
        return toMap(
                FilterDescriptor::getId,
                Function.identity(),
                FilterDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected Filter map(FilterDescriptor source) {
        return switch (source.getType()) {
            case TRANSACTION -> {
                TransactionFilterDescriptor txSource = (TransactionFilterDescriptor) source;
                yield new TransactionFilter(
                        source.getId(),
                        new FilterName(source.getName()),
                        source.getNodeId(),
                        txSource.getIdentifierType(),
                        txSource.getValue(),
                        txSource.getStatuses());
            }
            case EVENT -> {
                EventFilterDescriptor eventSource = (EventFilterDescriptor) source;
                EventFilterScope scope = eventSource.getScope();
                FilterName name = new FilterName(eventSource.getName());
                EventFilterSpecification specification =
                        new EventFilterSpecification(
                                eventSource.getSpecification().getSignature(),
                                eventSource.getSpecification().getCorrelationId() != null
                                        ? new CorrelationId(
                                                eventSource.getSpecification().getCorrelationId())
                                        : null);
                SyncState syncState =
                        eventSource.getSync() != null
                                ? (switch (eventSource.getSync().getStrategy()) {
                                    case BLOCK_BASED -> {
                                        BlockFilterSyncDescriptor blockSync =
                                                (BlockFilterSyncDescriptor) eventSource.getSync();
                                        yield new BlockActiveSyncState(
                                                new NonNegativeBlockNumber(
                                                        blockSync.getInitialBlock()),
                                                new NonNegativeBlockNumber(
                                                        BigInteger
                                                                .ZERO)); // TODO: Review last block
                                        // processed when
                                        // EventStore works
                                    }
                                })
                                : new NoSyncState();
                EventFilterVisibilityConfiguration visibility =
                        eventSource.getVisibility().getVisible()
                                ? EventFilterVisibilityConfiguration.visible()
                                : EventFilterVisibilityConfiguration.invisible(
                                        eventSource.getVisibility().getPrivacyGroupId());
                yield switch (scope) {
                    case GLOBAL ->
                            new GlobalEventFilter(
                                    eventSource.getId(),
                                    name,
                                    eventSource.getNodeId(),
                                    specification,
                                    eventSource.getStatuses(),
                                    syncState,
                                    visibility);
                    case CONTRACT ->
                            new ContractEventFilter(
                                    eventSource.getId(),
                                    name,
                                    eventSource.getNodeId(),
                                    specification,
                                    eventSource.getStatuses(),
                                    syncState,
                                    visibility,
                                    ((ContractEventFilterDescriptor) source).getAddress());
                };
            }
        };
    }
}
