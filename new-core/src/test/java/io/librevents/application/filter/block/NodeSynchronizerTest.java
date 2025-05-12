package io.librevents.application.filter.block;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.helper.ContractEventDispatcherHelper;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import io.librevents.domain.common.event.ContractEventStatus;
import io.librevents.domain.common.event.EventName;
import io.librevents.domain.filter.FilterName;
import io.librevents.domain.filter.FilterRepository;
import io.librevents.domain.filter.event.ContractEventFilter;
import io.librevents.domain.filter.event.EventFilterSpecification;
import io.librevents.domain.filter.event.parameter.AddressParameterDefinition;
import io.librevents.domain.filter.event.sync.block.BlockActiveSyncState;
import io.librevents.domain.filter.transaction.IdentifierType;
import io.librevents.domain.filter.transaction.TransactionFilter;
import io.librevents.domain.node.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NodeSynchronizerTest {

    @Mock private Node node;
    @Mock private Dispatcher dispatcher;
    @Mock private StartBlockCalculator calculator;
    @Mock private BlockInteractor blockInteractor;
    @Mock private FilterRepository filterRepository;
    @Mock private ContractEventParameterDecoder decoder;
    @Mock private ContractEventDispatcherHelper helper;

    @Test
    void testConstructor_nullValues() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                null,
                                dispatcher,
                                calculator,
                                blockInteractor,
                                filterRepository,
                                decoder,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                null,
                                calculator,
                                blockInteractor,
                                filterRepository,
                                decoder,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                dispatcher,
                                null,
                                blockInteractor,
                                filterRepository,
                                decoder,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                dispatcher,
                                calculator,
                                null,
                                filterRepository,
                                decoder,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                dispatcher,
                                calculator,
                                blockInteractor,
                                null,
                                decoder,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                dispatcher,
                                calculator,
                                blockInteractor,
                                filterRepository,
                                null,
                                helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node,
                                dispatcher,
                                calculator,
                                blockInteractor,
                                filterRepository,
                                decoder,
                                null));
    }

    @Test
    void testSynchronize() {
        UUID nodeId = UUID.randomUUID();
        when(node.getId()).thenReturn(nodeId);
        when(filterRepository.findByNodeId(nodeId))
                .thenReturn(
                        List.of(
                                new ContractEventFilter(
                                        UUID.randomUUID(),
                                        new FilterName("filter1"),
                                        nodeId,
                                        new EventFilterSpecification(
                                                new EventName("event1"),
                                                null,
                                                Set.of(new AddressParameterDefinition())),
                                        List.of(
                                                ContractEventStatus.CONFIRMED,
                                                ContractEventStatus.UNCONFIRMED),
                                        new BlockActiveSyncState(
                                                new NonNegativeBlockNumber(BigInteger.ZERO),
                                                new NonNegativeBlockNumber(BigInteger.ZERO)),
                                        "0x1234567890abcdef1234567890abcdef12345678")));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        dispatcher,
                        calculator,
                        blockInteractor,
                        filterRepository,
                        decoder,
                        helper);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }

    @Test
    void testSynchronize_noEventFilters() {
        UUID nodeId = UUID.randomUUID();
        when(node.getId()).thenReturn(nodeId);
        when(filterRepository.findByNodeId(nodeId))
                .thenReturn(
                        List.of(
                                new TransactionFilter(
                                        UUID.randomUUID(),
                                        new FilterName("filter1"),
                                        nodeId,
                                        IdentifierType.CONTRACT_ADDRESS,
                                        "0x1234567890abcdef1234567890abcdef12345678",
                                        List.of(TransactionStatus.FAILED))));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        dispatcher,
                        calculator,
                        blockInteractor,
                        filterRepository,
                        decoder,
                        helper);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }

    @Test
    void testSynchronize_filtersAlreadySynchronized() {
        UUID nodeId = UUID.randomUUID();
        BlockActiveSyncState syncState =
                new BlockActiveSyncState(
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO));
        syncState.setSync(true);
        when(node.getId()).thenReturn(nodeId);
        when(filterRepository.findByNodeId(nodeId))
                .thenReturn(
                        List.of(
                                new ContractEventFilter(
                                        UUID.randomUUID(),
                                        new FilterName("filter1"),
                                        nodeId,
                                        new EventFilterSpecification(
                                                new EventName("event1"),
                                                null,
                                                Set.of(new AddressParameterDefinition())),
                                        List.of(
                                                ContractEventStatus.CONFIRMED,
                                                ContractEventStatus.UNCONFIRMED),
                                        syncState,
                                        "0x1234567890abcdef1234567890abcdef12345678")));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(
                        node,
                        dispatcher,
                        calculator,
                        blockInteractor,
                        filterRepository,
                        decoder,
                        helper);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }
}
