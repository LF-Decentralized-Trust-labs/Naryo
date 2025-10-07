package io.naryo.application.filter.block;

import java.math.BigInteger;
import java.util.*;

import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;
import io.naryo.domain.filter.event.sync.block.BlockActiveFilterSyncState;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NodeSynchronizerTest {

    @Mock private LiveRegistry<Filter> filters;
    private final ResilienceRegistry resilienceRegistry = new ResilienceRegistry();
    @Mock private Node node;
    @Mock private StartBlockCalculator calculator;
    @Mock private BlockInteractor blockInteractor;
    @Mock private ContractEventParameterDecoder decoder;
    @Mock private ContractEventDispatcherHelper helper;
    private @Mock FilterStore<?> store;
    private @Mock StoreConfiguration storeConfiguration;

    @Test
    void testConstructor_nullValues() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                null,
                                calculator,
                                blockInteractor,
                                filters,
                                decoder,
                                helper,
                                resilienceRegistry,
                                store,
                                storeConfiguration));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                null,
                                blockInteractor,
                                filters,
                                decoder,
                                helper,
                                resilienceRegistry,
                                store,
                                storeConfiguration));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                calculator,
                                null,
                                filters,
                                decoder,
                                helper,
                                resilienceRegistry,
                                store,
                                storeConfiguration));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                calculator,
                                blockInteractor,
                                null,
                                decoder,
                                helper,
                                resilienceRegistry,
                                store,
                                storeConfiguration));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                calculator,
                                blockInteractor,
                                filters,
                                null,
                                helper,
                                resilienceRegistry,
                                store,
                                storeConfiguration));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                calculator,
                                blockInteractor,
                                filters,
                                decoder,
                                null,
                                resilienceRegistry,
                                store,
                                storeConfiguration));

        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                calculator,
                                blockInteractor,
                                filters,
                                decoder,
                                helper,
                                null,
                                store,
                                storeConfiguration));
    }

    @Test
    void testSynchronize() {
        UUID nodeId = UUID.randomUUID();
        Filter filter =
                new ContractEventFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        new EventFilterSpecification(
                                new EventName("event1"),
                                null,
                                Set.of(new AddressParameterDefinition())),
                        Set.of(ContractEventStatus.CONFIRMED, ContractEventStatus.UNCONFIRMED),
                        new BlockActiveFilterSyncState(new NonNegativeBlockNumber(BigInteger.ZERO)),
                        "0x1234567890abcdef1234567890abcdef12345678");

        when(filters.active()).thenReturn(new Revision<>(1, "test-hash", Set.of(filter)));

        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        calculator,
                        blockInteractor,
                        filters,
                        decoder,
                        helper,
                        resilienceRegistry,
                        store,
                        storeConfiguration);
        when(storeConfiguration.getState()).thenReturn(StoreState.INACTIVE);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }

    @Test
    void testSynchronize_noEventFilters() {
        UUID nodeId = UUID.randomUUID();
        Filter filter =
                new TransactionFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        IdentifierType.HASH,
                        "0x1234567890abcdef1234567890abcdef12345678",
                        Set.of(TransactionStatus.FAILED));

        when(filters.active()).thenReturn(new Revision<>(1, "test-hash", Set.of(filter)));

        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        calculator,
                        blockInteractor,
                        filters,
                        decoder,
                        helper,
                        resilienceRegistry,
                        store,
                        storeConfiguration);
        when(storeConfiguration.getState()).thenReturn(StoreState.INACTIVE);

        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }

    @Test
    void testSynchronize_filtersAlreadySynchronized() {
        UUID nodeId = UUID.randomUUID();
        BlockActiveFilterSyncState syncState =
                new BlockActiveFilterSyncState(new NonNegativeBlockNumber(BigInteger.ZERO));
        Filter filter =
                new ContractEventFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        new EventFilterSpecification(
                                new EventName("event1"),
                                null,
                                Set.of(new AddressParameterDefinition())),
                        Set.of(ContractEventStatus.CONFIRMED, ContractEventStatus.UNCONFIRMED),
                        syncState,
                        "0x1234567890abcdef1234567890abcdef12345678");

        when(filters.active()).thenReturn(new Revision<>(1, "test-hash", Set.of(filter)));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        calculator,
                        blockInteractor,
                        filters,
                        decoder,
                        helper,
                        resilienceRegistry,
                        store,
                        storeConfiguration);
        when(storeConfiguration.getState()).thenReturn(StoreState.INACTIVE);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }
}
