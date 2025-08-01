package io.naryo.application.filter.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.domain.node.Node;
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
    private CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("test");
    private Retry retry = Retry.ofDefaults("test");

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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            retry);
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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            retry);
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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            retry);
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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            retry);
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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            retry);
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
                            null,
                            circuitBreaker,
                            retry);
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
                            contractEventDispatcherHelper,
                            null,
                            retry);
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
                            contractEventDispatcherHelper,
                            circuitBreaker,
                            null);
                });
    }

    @Test
    void testSynchronize() throws IOException {
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
                        new TransactionReceipt(
                                "0xabcdef1234567890",
                                BigInteger.ONE,
                                "0xabcdef1234567890",
                                BigInteger.TEN,
                                BigInteger.ONE,
                                BigInteger.ONE,
                                "0xabcdef1234567890",
                                "0x0",
                                "0xabcdef1234567890",
                                "0xabcdef1234567890",
                                List.of(),
                                "0x0",
                                "0x0",
                                "0x0"));
        when(blockInteractor.getBlock(any(BigInteger.class)))
                .thenReturn(
                        new Block(
                                BigInteger.ONE,
                                "0x123",
                                "1000",
                                BigInteger.ZERO,
                                BigInteger.TEN,
                                BigInteger.TEN,
                                List.of()));

        EventFilterSynchronizer synchronizer =
                new EventFilterSynchronizer(
                        node,
                        eventFilter,
                        blockInteractor,
                        startBlockCalculator,
                        contractEventParameterDecoder,
                        contractEventDispatcherHelper,
                        circuitBreaker,
                        retry);
        assertDoesNotThrow(synchronizer::synchronize);
    }
}
