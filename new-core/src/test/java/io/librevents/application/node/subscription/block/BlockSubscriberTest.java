package io.librevents.application.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.UUID;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.connection.http.*;
import io.librevents.domain.node.ethereum.pub.PublicEthereumNode;
import io.librevents.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.librevents.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class BlockSubscriberTest {

    @Mock protected BlockInteractor interactor;

    @Mock protected Dispatcher dispatcher;

    @Mock protected BlockToBlockEventMapper blockMapper;

    protected abstract BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper);

    @Test
    void testConstructorWithNullInteractor() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        null, dispatcher, newNode(0, 0, 0, 0), blockMapper));
        assertEquals("interactor cannot be null", ex.getMessage());
    }

    @Test
    void testConstructorWithNullDispatcher() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        interactor, null, newNode(0, 0, 0, 0), blockMapper));
        assertEquals("dispatcher cannot be null", ex.getMessage());
    }

    @Test
    void testConstructorWithNullBlockMapper() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        interactor, dispatcher, newNode(0, 0, 0, 0), null));
        assertEquals("blockMapper cannot be null", ex.getMessage());
    }

    @Test
    void testGetStartBlockWithZeroLatestBlock() {
        BlockSubscriber subscriber =
                createBlockSubscriber(interactor, dispatcher, newNode(10, 0, 0, 0), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.TEN, startBlock);
    }

    @Test
    void testGetStartBlockWithNonZeroLatestBlock() {
        BlockSubscriber subscriber =
                createBlockSubscriber(interactor, dispatcher, newNode(10, 1, 0, 0), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.ONE, startBlock);
    }

    @Test
    void testGetStartBlockWithSyncBlockLimit() {
        when(interactor.getCurrentBlockNumber()).thenReturn(BigInteger.valueOf(1000));
        BlockSubscriber subscriber =
                createBlockSubscriber(interactor, dispatcher, newNode(0, 100, 10, 0), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(990), startBlock);
    }

    @Test
    void testGetStartBlockWithSyncLimitExceedCurrentBlock() {
        when(interactor.getCurrentBlockNumber()).thenReturn(BigInteger.valueOf(101));
        BlockSubscriber subscriber =
                createBlockSubscriber(interactor, dispatcher, newNode(0, 100, 10, 0), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }

    @Test
    void testGetStartBlockWithReplayOffsetExceeding() {
        BlockSubscriber subscriber =
                createBlockSubscriber(
                        interactor, dispatcher, newNode(0, 100, 0, 1000), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.ZERO, startBlock);
    }

    @Test
    void testGetStartBlockWithCurrentBlock() {
        BlockSubscriber subscriber =
                createBlockSubscriber(interactor, dispatcher, newNode(0, 100, 0, 0), blockMapper);
        BigInteger startBlock = subscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }

    protected Node newNode(long start, long latest, long syncLimit, long replayOffset) {
        return new PublicEthereumNode(
                UUID.randomUUID(),
                new NodeName("MockNode"),
                new BlockSubscriptionConfiguration(
                        new PubSubBlockSubscriptionMethodConfiguration(),
                        new NonNegativeBlockNumber(BigInteger.valueOf(start)),
                        new NonNegativeBlockNumber(BigInteger.valueOf(latest)),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
                        new NonNegativeBlockNumber(BigInteger.valueOf(replayOffset)),
                        new NonNegativeBlockNumber(BigInteger.valueOf(syncLimit))),
                new EthereumRpcBlockInteractionConfiguration(),
                new HttpNodeConnection(
                        new ConnectionEndpoint("http://localhost:8545"),
                        new RetryConfiguration(1, Duration.ofSeconds(1)),
                        new MaxIdleConnections(1),
                        new KeepAliveDuration(Duration.ofSeconds(1)),
                        new ConnectionTimeout(Duration.ofSeconds(1)),
                        new ReadTimeout(Duration.ofSeconds(1))));
    }
}
