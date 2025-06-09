package io.naryo.infrastructure.configuration.provider.filter;

import java.math.BigInteger;
import java.util.Collection;

import io.naryo.application.filter.configuration.provider.FilterConfigurationProvider;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.contract.ContractEventFilterConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncType;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block.BlockSyncConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.transaction.TransactionFilterConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvFilterConfigurationProvider implements FilterConfigurationProvider {

    private final EnvironmentProperties properties;

    public EnvFilterConfigurationProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<Filter> load() {
        return properties.filters().stream().map(this::mapFilter).toList();
    }

    @Override
    public int priority() {
        return 0;
    }

    private Filter mapFilter(FilterProperties props) {
        switch (props.type()) {
            case EVENT -> {
                EventFilterConfigurationProperties config =
                        (EventFilterConfigurationProperties) props.configuration();
                return switch (config.scope()) {
                    case GLOBAL ->
                            new GlobalEventFilter(
                                    props.id(),
                                    new FilterName(props.name()),
                                    props.nodeId(),
                                    mapSpecification(config.specification()),
                                    config.statuses(),
                                    mapSync(config.sync()));
                    case CONTRACT -> {
                        ContractEventFilterConfigurationAdditionalProperties addProps =
                                (ContractEventFilterConfigurationAdditionalProperties)
                                        config.configuration();
                        yield new ContractEventFilter(
                                props.id(),
                                new FilterName(props.name()),
                                props.nodeId(),
                                mapSpecification(config.specification()),
                                config.statuses(),
                                mapSync(config.sync()),
                                addProps.address());
                    }
                };
            }
            case TRANSACTION -> {
                TransactionFilterConfigurationProperties config =
                        (TransactionFilterConfigurationProperties) props.configuration();
                return new TransactionFilter(
                        props.id(),
                        new FilterName(props.name()),
                        props.nodeId(),
                        config.identifierType(),
                        config.value(),
                        config.statuses());
            }
        }

        return null;
    }

    private SyncState mapSync(SyncConfigurationProperties sync) {
        SyncState syncState = new NoSyncState();
        if (sync.type() == SyncType.BLOCK_BASED) {
            BlockSyncConfigurationAdditionalProperties props =
                    (BlockSyncConfigurationAdditionalProperties) sync.configuration();
            syncState =
                    new BlockActiveSyncState(
                            new NonNegativeBlockNumber(props.initialBlock()),
                            new NonNegativeBlockNumber(BigInteger.ZERO));
        }
        return syncState;
    }

    private EventFilterSpecification mapSpecification(EventSpecification configSpec) {
        return new EventFilterSpecification(
                configSpec.signature(), new CorrelationId(configSpec.correlationId().position()));
    }
}
