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
import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.Event;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.NodeType;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.connection.http.*;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.librevents.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BlockSubscriberTest {

    protected static class MockBlockInteractor implements BlockInteractor {

        private final BigInteger latestBlock;

        public MockBlockInteractor() {
            this.latestBlock = BigInteger.ZERO;
        }

        public MockBlockInteractor(BigInteger latestBlock) {
            this.latestBlock = latestBlock;
        }

        @Override
        public Flowable<Block> replayPastBlocks(BigInteger startBlock) {
            Block block = new Block(
                BigInteger.ONE,
                "0x0",
                BigInteger.ZERO,
                "0x0",
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                List.of()
            );
            return Flowable.just(block);
        }

        @Override
        public Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock) {
            Block block = new Block(
                BigInteger.ONE,
                "0x0",
                BigInteger.ZERO,
                "0x0",
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                List.of()
            );
            return Flowable.just(block);
        }

        @Override
        public Flowable<Block> replyFutureBlocks() {
            Block block = new Block(
                BigInteger.ONE,
                "0x0",
                BigInteger.ZERO,
                "0x0",
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                List.of()
            );
            return Flowable.just(block);
        }

        @Override
        public Block getCurrentBlock() {
            return null;
        }

        @Override
        public BigInteger getCurrentBlockNumber() {
            return latestBlock;
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

    public static class MockDispatcher implements Dispatcher {
        @Override
        public void dispatch(Event event) {}

        @Override
        public void addTrigger(Trigger<?> trigger) {}

        @Override
        public void removeTrigger(Trigger<?> trigger) {}
    }

    protected static class MockInteractionConfiguration extends InteractionConfiguration {

        protected MockInteractionConfiguration() {
            super(InteractionStrategy.BLOCK_BASED);
        }
    }

    protected static class MockNode extends Node {

        protected MockNode() {
            this(BigInteger.ZERO, BigInteger.ZERO);
        }

        public MockNode(BigInteger startBlock, BigInteger latestBlock) {
            this(startBlock, latestBlock, BigInteger.ZERO);
        }

        protected MockNode(
                BigInteger startBlock, BigInteger latestBlock, BigInteger syncBlockLimit) {
            this(startBlock, latestBlock, syncBlockLimit, BigInteger.ZERO);
        }

        protected MockNode(
                BigInteger startBlock,
                BigInteger latestBlock,
                BigInteger syncBlockLimit,
                BigInteger replayBlockOffset) {
            super(
                    UUID.randomUUID(),
                    new NodeName("MockNode"),
                    NodeType.ETHEREUM,
                    new BlockSubscriptionConfiguration(
                            new PubSubBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(startBlock),
                            new NonNegativeBlockNumber(latestBlock),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(replayBlockOffset),
                            new NonNegativeBlockNumber(syncBlockLimit)),
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

    protected abstract BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper);

    @Test
    void testConstructorWithNullInteractor() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    createBlockSubscriber(
                            null,
                            new MockDispatcher(),
                            new MockNode(),
                            new BlockToBlockEventMapper());
                });
    }

    @Test
    void testConstructorWithNullDispatcher() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    createBlockSubscriber(
                            new MockBlockInteractor(),
                            null,
                            new MockNode(),
                            new BlockToBlockEventMapper());
                });
    }

    @Test
    void testConstructorWithNullBlockMapper() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    createBlockSubscriber(
                            new MockBlockInteractor(), new MockDispatcher(), new MockNode(), null);
                });
    }

    @Test
    void testGetStartBlockWithZeroLatestBlock() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(),
                        new MockDispatcher(),
                        new MockNode(BigInteger.TEN, BigInteger.ZERO),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.TEN, startBlock);
    }

    @Test
    void testGetStartBlockWithNonZeroLatestBlock() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(),
                        new MockDispatcher(),
                        new MockNode(BigInteger.TEN, BigInteger.ONE),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.ONE, startBlock);
    }

    @Test
    void testGetStartBlockWithInitialBlock() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(),
                        new MockDispatcher(),
                        new MockNode(BigInteger.TEN, BigInteger.ZERO),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.TEN, startBlock);
    }

    @Test
    void testGetStartBlockWithSyncBlockLimit() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(BigInteger.valueOf(1000)),
                        new MockDispatcher(),
                        new MockNode(BigInteger.ZERO, BigInteger.valueOf(100), BigInteger.TEN),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(990), startBlock);
    }

    @Test
    void testGetStartBlockWithSyncBlockLimitExceedingCurrentBlock() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(BigInteger.valueOf(101)),
                        new MockDispatcher(),
                        new MockNode(BigInteger.ZERO, BigInteger.valueOf(100), BigInteger.TEN),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }

    @Test
    void testGetStartBlockWithExceedingReplayOffset() {
        BlockSubscriber blockSubscriber =
                createBlockSubscriber(
                        new MockBlockInteractor(BigInteger.valueOf(100)),
                        new MockDispatcher(),
                        new MockNode(
                                BigInteger.ZERO,
                                BigInteger.valueOf(100),
                                BigInteger.ZERO,
                                BigInteger.valueOf(1000)),
                        new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.ZERO, startBlock);
    }

    @Test
    void testGetStartBlockWithCurrentBlock() {
        BlockSubscriber blockSubscriber =
            createBlockSubscriber(
                new MockBlockInteractor(BigInteger.valueOf(100)),
                new MockDispatcher(),
                new MockNode(),
                new BlockToBlockEventMapper());
        BigInteger startBlock = blockSubscriber.getStartBlock();
        assertEquals(BigInteger.valueOf(100), startBlock);
    }
}
