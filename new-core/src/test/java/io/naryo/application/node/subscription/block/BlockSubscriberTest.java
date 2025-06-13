package io.naryo.application.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.UUID;

import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public abstract class BlockSubscriberTest {

    @Mock protected BlockInteractor interactor;

    @Mock protected Dispatcher dispatcher;

    @Mock protected BlockToBlockEventMapper blockMapper;

    @Mock protected StartBlockCalculator calculator;

    protected abstract BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper,
            StartBlockCalculator calculator);

    @Test
    void testConstructorWithNullInteractor() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        null,
                                        dispatcher,
                                        newNode(0, 0, 0),
                                        blockMapper,
                                        calculator));
        assertEquals("interactor cannot be null", ex.getMessage());
    }

    @Test
    void testConstructorWithNullDispatcher() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        interactor,
                                        null,
                                        newNode(0, 0, 0),
                                        blockMapper,
                                        calculator));
        assertEquals("dispatcher cannot be null", ex.getMessage());
    }

    @Test
    void testConstructorWithNullBlockMapper() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                createBlockSubscriber(
                                        interactor,
                                        dispatcher,
                                        newNode(0, 0, 0),
                                        null,
                                        calculator));
        assertEquals("blockMapper cannot be null", ex.getMessage());
    }

    protected Node newNode(long start, long syncLimit, long replayOffset) {
        return new PublicEthereumNode(
                UUID.randomUUID(),
                new NodeName("MockNode"),
                new BlockSubscriptionConfiguration(
                        new PubSubBlockSubscriptionMethodConfiguration(),
                        BigInteger.valueOf(start),
                        new NonNegativeBlockNumber(BigInteger.ZERO),
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
