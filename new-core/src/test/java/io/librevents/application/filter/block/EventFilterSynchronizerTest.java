package io.librevents.application.filter.block;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.helper.ContractEventDispatcherHelper;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.domain.common.EventName;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.EventFilterSpecification;
import io.librevents.domain.filter.event.parameter.AddressParameterDefinition;
import io.librevents.domain.filter.event.sync.block.BlockActiveSyncState;
import io.librevents.domain.node.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventFilterSynchronizerTest {

    private @Mock Node node;
    private @Mock EventFilter eventFilter;
    private @Mock BlockInteractor blockInteractor;
    private @Mock StartBlockCalculator startBlockCalculator;
    private @Mock ContractEventParameterDecoder contractEventParameterDecoder;
    private @Mock ContractEventDispatcherHelper contractEventDispatcherHelper;

    @Test
    void testConstructor_nullValues() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            null,
                            eventFilter,
                            blockInteractor,
                            startBlockCalculator,
                            contractEventParameterDecoder,
                            contractEventDispatcherHelper);
                });

        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            node,
                            null,
                            blockInteractor,
                            startBlockCalculator,
                            contractEventParameterDecoder,
                            contractEventDispatcherHelper);
                });

        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            node,
                            eventFilter,
                            null,
                            startBlockCalculator,
                            contractEventParameterDecoder,
                            contractEventDispatcherHelper);
                });

        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            node,
                            eventFilter,
                            blockInteractor,
                            null,
                            contractEventParameterDecoder,
                            contractEventDispatcherHelper);
                });

        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            node,
                            eventFilter,
                            blockInteractor,
                            startBlockCalculator,
                            null,
                            contractEventDispatcherHelper);
                });

        assertThrows(
                NullPointerException.class,
                () -> {
                    new EventFilterSynchronizer(
                            node,
                            eventFilter,
                            blockInteractor,
                            startBlockCalculator,
                            contractEventParameterDecoder,
                            null);
                });
    }

    @Test
    void testSynchronize() {
        when(eventFilter.getNodeId()).thenReturn(UUID.randomUUID());
        when(eventFilter.getSpecification())
                .thenReturn(
                        new EventFilterSpecification(
                                new EventName("Test"),
                                null,
                                Set.of(new AddressParameterDefinition())));
        when(eventFilter.getSyncState())
                .thenReturn(
                        new BlockActiveSyncState(
                                new NonNegativeBlockNumber(BigInteger.TEN),
                                new NonNegativeBlockNumber(BigInteger.ZERO)));
        when(startBlockCalculator.getStartBlock()).thenReturn(BigInteger.valueOf(100));
        when(blockInteractor.getLogs(any(), any(), anyList()))
                .thenReturn(
                        List.of(
                                new Log(
                                        BigInteger.TEN,
                                        BigInteger.ZERO,
                                        "0xabcdef1234567890",
                                        "0xabcdef1234567890",
                                        BigInteger.TEN,
                                        "0xabcdef1234567890",
                                        "",
                                        "",
                                        List.of("0xabcdef1234567890"))));
        when(blockInteractor.getTransactionReceipt(any()))
                .thenReturn(
                        new Transaction(
                                "0xabcdef1234567890",
                                BigInteger.ONE,
                                BigInteger.TEN,
                                "0xabcdef1234567890",
                                "0xabcdef1234567890",
                                ""));
        when(blockInteractor.getBlock(any(BigInteger.class)))
                .thenReturn(
                        new Block(
                                BigInteger.ONE,
                                "0x123",
                                BigInteger.ONE,
                                "1000",
                                BigInteger.ZERO,
                                BigInteger.TEN,
                                BigInteger.TEN,
                                BigInteger.ZERO,
                                List.of()));

        EventFilterSynchronizer synchronizer =
                new EventFilterSynchronizer(
                        node,
                        eventFilter,
                        blockInteractor,
                        startBlockCalculator,
                        contractEventParameterDecoder,
                        contractEventDispatcherHelper);
        assertDoesNotThrow(synchronizer::synchronize);
    }
}
