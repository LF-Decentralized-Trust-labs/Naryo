package io.naryo.application.filter.block;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NodeSynchronizerTest {

    private final List<Filter> filters = new ArrayList<>();
    @Mock private Node node;
    @Mock private StartBlockCalculator calculator;
    @Mock private BlockInteractor blockInteractor;
    @Mock private ContractEventParameterDecoder decoder;
    @Mock private ContractEventDispatcherHelper helper;

    @AfterEach
    void cleanUp() {
        filters.clear();
    }

    @Test
    void testConstructor_nullValues() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                null, calculator, blockInteractor, filters, decoder, helper));
        assertThrows(
                NullPointerException.class,
                () -> new NodeSynchronizer(node, null, blockInteractor, filters, decoder, helper));
        assertThrows(
                NullPointerException.class,
                () -> new NodeSynchronizer(node, calculator, null, filters, decoder, helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node, calculator, blockInteractor, null, decoder, helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node, calculator, blockInteractor, filters, null, helper));
        assertThrows(
                NullPointerException.class,
                () ->
                        new NodeSynchronizer(
                                node, calculator, blockInteractor, filters, decoder, null));
    }

    @Test
    void testSynchronize() {
        UUID nodeId = UUID.randomUUID();
        filters.add(
                new ContractEventFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        new EventFilterSpecification(
                                new EventName("event1"),
                                null,
                                Set.of(new AddressParameterDefinition())),
                        List.of(ContractEventStatus.CONFIRMED, ContractEventStatus.UNCONFIRMED),
                        new BlockActiveSyncState(
                                new NonNegativeBlockNumber(BigInteger.ZERO),
                                new NonNegativeBlockNumber(BigInteger.ZERO)),
                        "0x1234567890abcdef1234567890abcdef12345678"));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(node, calculator, blockInteractor, filters, decoder, helper);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }

    @Test
    void testSynchronize_noEventFilters() {
        UUID nodeId = UUID.randomUUID();
        filters.add(
                new TransactionFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        IdentifierType.HASH,
                        "0x1234567890abcdef1234567890abcdef12345678",
                        List.of(TransactionStatus.FAILED)));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(node, calculator, blockInteractor, filters, decoder, helper);
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
        filters.add(
                new ContractEventFilter(
                        UUID.randomUUID(),
                        new FilterName("filter1"),
                        nodeId,
                        new EventFilterSpecification(
                                new EventName("event1"),
                                null,
                                Set.of(new AddressParameterDefinition())),
                        List.of(ContractEventStatus.CONFIRMED, ContractEventStatus.UNCONFIRMED),
                        syncState,
                        "0x1234567890abcdef1234567890abcdef12345678"));
        NodeSynchronizer synchronizer =
                new NodeSynchronizer(node, calculator, blockInteractor, filters, decoder, helper);
        assertDoesNotThrow(
                () -> {
                    synchronizer.synchronize();
                });
    }
}
