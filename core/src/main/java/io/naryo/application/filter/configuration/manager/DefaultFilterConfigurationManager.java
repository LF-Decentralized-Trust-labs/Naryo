package io.naryo.application.filter.configuration.manager;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.application.filter.configuration.provider.FilterSourceProvider;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import jakarta.annotation.Nullable;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;
import static java.util.stream.Collectors.toMap;

public final class DefaultFilterConfigurationManager
        extends BaseCollectionConfigurationManager<Filter, FilterDescriptor, UUID>
        implements FilterConfigurationManager {

    public DefaultFilterConfigurationManager(
            List<FilterSourceProvider> collectionConfigurationProviders) {
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
        return switch (source) {
            case TransactionFilterDescriptor txSource ->
                    getTransactionFilterFromDescriptor(txSource);
            case EventFilterDescriptor eventSource -> getEventFilterFromDescriptor(eventSource);
            default ->
                    throw new IllegalArgumentException(
                            "Unknown filter descriptor: " + source.getType());
        };
    }

    private TransactionFilter getTransactionFilterFromDescriptor(
            TransactionFilterDescriptor descriptor) {
        FilterName name = new FilterName(valueOrNull(FilterDescriptor::getName, descriptor));
        UUID nodeId = valueOrNull(FilterDescriptor::getNodeId, descriptor);
        IdentifierType identifierType =
                valueOrNull(TransactionFilterDescriptor::getIdentifierType, descriptor);
        String value = valueOrNull(TransactionFilterDescriptor::getValue, descriptor);
        Set<TransactionStatus> statuses = descriptor.getStatuses();

        return new TransactionFilter(
                descriptor.getId(), name, nodeId, identifierType, value, statuses);
    }

    private EventFilter getEventFilterFromDescriptor(EventFilterDescriptor descriptor) {
        FilterName name = new FilterName(valueOrNull(FilterDescriptor::getName, descriptor));
        UUID nodeId = valueOrNull(FilterDescriptor::getNodeId, descriptor);
        EventFilterSpecification specification =
                getFilterSpecificationFromDescriptor(
                        valueOrNull(EventFilterDescriptor::getSpecification, descriptor));
        SyncState syncState =
                getSyncStateFromDescriptor(valueOrNull(EventFilterDescriptor::getSync, descriptor));
        EventFilterVisibilityConfiguration visibilityConfiguration =
                getVisibilityConfigurationFromDescriptor(
                        valueOrNull(EventFilterDescriptor::getVisibility, descriptor));

        return switch (descriptor) {
            case GlobalEventFilterDescriptor ignored ->
                    new GlobalEventFilter(
                            descriptor.getId(),
                            name,
                            nodeId,
                            specification,
                            descriptor.getStatuses(),
                            syncState,
                            visibilityConfiguration);
            case ContractEventFilterDescriptor contractDescriptor -> {
                String address =
                        valueOrNull(ContractEventFilterDescriptor::getAddress, contractDescriptor);
                yield new ContractEventFilter(
                        descriptor.getId(),
                        name,
                        nodeId,
                        specification,
                        descriptor.getStatuses(),
                        syncState,
                        visibilityConfiguration,
                        address);
            }
            default ->
                    throw new IllegalArgumentException(
                            "Unknown event filter with scope: " + descriptor.getScope());
        };
    }

    private EventFilterSpecification getFilterSpecificationFromDescriptor(
            EventSpecificationDescriptor descriptor) {
        String signature = valueOrNull(EventSpecificationDescriptor::getSignature, descriptor);
        CorrelationId correlationId =
                descriptor.getCorrelationId().map(CorrelationId::new).orElse(null);

        return new EventFilterSpecification(signature, correlationId);
    }

    private SyncState getSyncStateFromDescriptor(@Nullable FilterSyncDescriptor descriptor) {
        if (descriptor == null) {
            return new NoSyncState();
        }

        if (descriptor instanceof BlockFilterSyncDescriptor blockSync) {
            return new BlockActiveSyncState(
                    new NonNegativeBlockNumber(valueOrNull(blockSync.getInitialBlock())));
        }

        throw new IllegalArgumentException("Unknown sync strategy: " + descriptor.getStrategy());
    }

    private EventFilterVisibilityConfiguration getVisibilityConfigurationFromDescriptor(
            FilterVisibilityDescriptor descriptor) {
        return descriptor
                .getVisible()
                .map(
                        isVisible -> {
                            if (isVisible) {
                                return EventFilterVisibilityConfiguration.visible();
                            } else {
                                String privacyGroupId =
                                        valueOrNull(
                                                FilterVisibilityDescriptor::getPrivacyGroupId,
                                                descriptor);
                                return EventFilterVisibilityConfiguration.invisible(privacyGroupId);
                            }
                        })
                .orElse(EventFilterVisibilityConfiguration.visible());
    }
}
