package io.librevents.application.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.librevents.application.node.subscription.block.poll.PollBlockSubscriber;
import io.librevents.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.event.Event;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.NodeType;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.connection.http.*;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockSubscriberFactoryTest {

    private static class MockDispatcher implements Dispatcher {

        @Override
        public void dispatch(Event event) {
            // Mock implementation
        }

        @Override
        public void addTrigger(Trigger<?> trigger) {
            // Mock implementation

        }

        @Override
        public void removeTrigger(Trigger<?> trigger) {
            // Mock implementation
        }
    }

    private static class MockBlockInteractor implements BlockInteractor {

        @Override
        public Flowable<Block> replayPastBlocks(BigInteger startBlock) {
            return null;
        }

        @Override
        public Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock) {
            return null;
        }

        @Override
        public Flowable<Block> replyFutureBlocks() {
            return null;
        }

        @Override
        public Block getCurrentBlock() {
            return null;
        }

        @Override
        public BigInteger getCurrentBlockNumber() {
            return null;
        }

        @Override
        public Block getBlock(BigInteger number) {
            return null;
        }

        @Override
        public Block getBlock(String hash) {
            return null;
        }

        @Override
        public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock) {
            return List.of();
        }

        @Override
        public List<Log> getLogs(
                BigInteger startBlock, BigInteger endBlock, String contractAddress) {
            return List.of();
        }

        @Override
        public List<Log> getLogs(String blockHash) {
            return List.of();
        }

        @Override
        public List<Log> getLogs(String blockHash, String contractAddress) {
            return List.of();
        }
    }

    private static class MockSubscriptionConfiguration extends SubscriptionConfiguration {

        protected MockSubscriptionConfiguration() {
            super(SubscriptionStrategy.BLOCK_BASED);
        }
    }

    private static class MockInteractionConfiguration extends InteractionConfiguration {

        protected MockInteractionConfiguration() {
            super(InteractionStrategy.BLOCK_BASED);
        }
    }

    private static class MockNode extends Node {

        protected MockNode() {
            super(
                    UUID.randomUUID(),
                    new NodeName("MockNode"),
                    NodeType.ETHEREUM,
                    new MockSubscriptionConfiguration(),
                    new MockInteractionConfiguration(),
                    new HttpNodeConnection(
                            new ConnectionEndpoint("http://localhost:8545"),
                            new RetryConfiguration(1, Duration.ofSeconds(1)),
                            new MaxIdleConnections(1),
                            new KeepAliveDuration(Duration.ofSeconds(1)),
                            new ConnectionTimeout(Duration.ofSeconds(1)),
                            new ReadTimeout(Duration.ofSeconds(1))));
        }
    }

    @Test
    void testConstructorWithNullInteractor() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockSubscriberFactory(
                                null, new MockDispatcher(), new BlockToBlockEventMapper()));
    }

    @Test
    void testConstructorWithNullDispatcher() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockSubscriberFactory(
                                new MockBlockInteractor(), null, new BlockToBlockEventMapper()));
    }

    @Test
    void testConstructorWithNullBlockMapper() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockSubscriberFactory(
                                new MockBlockInteractor(), new MockDispatcher(), null));
    }

    @Test
    void testCreateWithNullMethod() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockSubscriberFactory(
                                        new MockBlockInteractor(),
                                        new MockDispatcher(),
                                        new BlockToBlockEventMapper())
                                .create(null, new MockNode()));
    }

    @Test
    void testCreateWithNullNode() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockSubscriberFactory(
                                        new MockBlockInteractor(),
                                        new MockDispatcher(),
                                        new BlockToBlockEventMapper())
                                .create(BlockSubscriptionMethod.POLL, null));
    }

    @Test
    void testCreateWithPollMethod() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(
                        new MockBlockInteractor(),
                        new MockDispatcher(),
                        new BlockToBlockEventMapper());
        BlockSubscriber subscriber = factory.create(BlockSubscriptionMethod.POLL, new MockNode());
        assertTrue(subscriber instanceof PollBlockSubscriber);
    }

    @Test
    void testCreateWithPubSubMethod() {
        BlockSubscriberFactory factory =
                new BlockSubscriberFactory(
                        new MockBlockInteractor(),
                        new MockDispatcher(),
                        new BlockToBlockEventMapper());
        BlockSubscriber subscriber = factory.create(BlockSubscriptionMethod.PUBSUB, new MockNode());
        assertTrue(subscriber instanceof PubSubBlockSubscriber);
    }
}
