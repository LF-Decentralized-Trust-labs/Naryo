package io.naryo.application.node.calculator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartBlockCalculatorTest {

    @Mock private BlockInteractor interactor;
    @Mock private Node node;
    @Mock private BlockSubscriptionConfiguration configuration;
    @Mock private BlockEventStoreConfiguration eventStoreConfiguration;
    @Mock private BlockEventStore<BlockEventStoreConfiguration> blockEventStore;

    @Test
    void testConstructorWithNullNode() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new StartBlockCalculator(
                                        null,
                                        interactor,
                                        blockEventStore,
                                        eventStoreConfiguration));
        assertEquals("node must not be null", ex.getMessage());
    }

    @Test
    void testConstructorWithNullInteractor() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new StartBlockCalculator(
                                        node, null, blockEventStore, eventStoreConfiguration));
        assertEquals("interactor must not be null", ex.getMessage());
    }

    @Test
    void testGetStartBlockWithZeroLatestBlock() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.ZERO));
        when(configuration.getInitialBlock()).thenReturn(BigInteger.TEN);
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.TEN, startBlock);
    }

    @Test
    void testGetStartBlockWithCurrentBlock() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.ZERO));
        when(configuration.getInitialBlock()).thenReturn(BigInteger.valueOf(-1));
        when(interactor.getCurrentBlockNumber()).thenReturn(BigInteger.TWO);
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.TWO, startBlock);
    }

    @Test
    void testGetStartBlockWithNonZeroLatestBlock() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.TEN));
        when(configuration.getReplayBlockOffset())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.TWO));
        when(configuration.getSyncBlockLimit())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.ZERO));
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.valueOf(8), startBlock);
    }

    @Test
    void testGetStartBlockWithSyncBlockLimit() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.TEN));
        when(configuration.getReplayBlockOffset())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.ZERO));
        when(configuration.getSyncBlockLimit())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.valueOf(100)));
        when(interactor.getCurrentBlockNumber()).thenReturn(BigInteger.valueOf(200));
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }

    @Test
    void testGetStartBlockWithSyncLimitExceedCurrentBlock() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(interactor.getCurrentBlockNumber()).thenReturn(BigInteger.valueOf(101));
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.valueOf(100)));
        when(configuration.getReplayBlockOffset())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.ZERO));
        when(configuration.getSyncBlockLimit())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.valueOf(10)));
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }

    @Test
    void testGetStartBlockWithReplayOffsetExceeding() throws IOException {
        when(node.getSubscriptionConfiguration()).thenReturn(configuration);
        when(blockEventStore.getLastestBlock(eventStoreConfiguration))
                .thenReturn(Optional.of(BigInteger.valueOf(100)));
        when(configuration.getReplayBlockOffset())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.valueOf(1000)));
        when(configuration.getSyncBlockLimit())
                .thenReturn(new NonNegativeBlockNumber(BigInteger.ZERO));
        StartBlockCalculator calculator =
                new StartBlockCalculator(
                        node, interactor, blockEventStore, eventStoreConfiguration);
        BigInteger startBlock = calculator.getStartBlock();
        assertEquals(BigInteger.ZERO, startBlock);
    }
}
