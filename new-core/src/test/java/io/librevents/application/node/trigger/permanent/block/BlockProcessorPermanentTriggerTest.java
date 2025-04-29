package io.librevents.application.node.trigger.permanent.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.librevents.application.event.decoder.block.DefaultContractEventParameterDecoder;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.common.EventName;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.filter.FilterName;
import io.librevents.domain.filter.event.*;
import io.librevents.domain.filter.event.parameter.AddressParameterDefinition;
import io.librevents.domain.filter.event.sync.NoSyncState;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.NodeRepository;
import io.librevents.domain.node.NodeType;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.connection.http.*;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.librevents.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockProcessorPermanentTriggerTest {

    private final DefaultContractEventParameterDecoder decoder =
            new DefaultContractEventParameterDecoder();

    private static class MockInteractionConfiguration extends InteractionConfiguration {

        private MockInteractionConfiguration() {
            super(InteractionStrategy.BLOCK_BASED);
        }
    }

    private static class MockNode extends Node {

        public MockNode() {
            this(BigInteger.ZERO);
        }

        public MockNode(BigInteger confirmationBlocks) {
            super(
                    UUID.randomUUID(),
                    new NodeName("MockNode"),
                    NodeType.ETHEREUM,
                    new BlockSubscriptionConfiguration(
                            new PubSubBlockSubscriptionMethodConfiguration(),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(confirmationBlocks),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO),
                            new NonNegativeBlockNumber(BigInteger.ZERO)),
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

    private static class MockBlockInteractor implements BlockInteractor {
        private final Log log;

        public MockBlockInteractor() {
            this.log = null;
        }

        public MockBlockInteractor(Log log) {
            this.log = log;
        }

        @Override
        public io.reactivex.Flowable<Block> replayPastBlocks(BigInteger startBlock) {
            return null;
        }

        @Override
        public io.reactivex.Flowable<Block> replayPastAndFutureBlocks(BigInteger startBlock) {
            return null;
        }

        @Override
        public io.reactivex.Flowable<Block> replyFutureBlocks() {
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
            return log != null ? List.of(log) : List.of();
        }

        @Override
        public List<Log> getLogs(String blockHash, String contractAddress) {
            return List.of();
        }
    }

    public static class TestDispatcher implements Dispatcher {

        private final AtomicBoolean dispatched = new AtomicBoolean(false);

        @Override
        public void dispatch(Event event) {
            dispatched.set(true);
        }

        @Override
        public void addTrigger(Trigger<?> trigger) {}

        @Override
        public void removeTrigger(Trigger<?> trigger) {}

        public boolean isDispatched() {
            return dispatched.get();
        }
    }

    private static class DefaultNodeRepository implements NodeRepository {
        private final Node node;

        private DefaultNodeRepository() {
            this.node = null;
        }

        private DefaultNodeRepository(Node node) {
            this.node = node;
        }

        @Override
        public Optional<Node> findById(UUID id) {
            return node != null ? Optional.of(node) : Optional.empty();
        }
    }

    private static BlockEvent createBlockEvent(UUID nodeId) {
        return new BlockEvent(
                nodeId,
                new NonNegativeBlockNumber(BigInteger.ZERO),
                "0x0",
                "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000100000000000000000002000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                List.of(
                        new Transaction(
                                "0x0", BigInteger.ONE, BigInteger.ZERO, "0x0", "0x0", "0x0")));
    }

    @Test
    void testConstructor() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        assertNotNull(trigger);
    }

    @Test
    void testConstructorWithNullFilters() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger(
                                null,
                                new MockBlockInteractor(),
                                new TestDispatcher(),
                                decoder,
                                new DefaultNodeRepository()));
    }

    @Test
    void testConstructorWithNullBlockInteractor() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger(
                                List.of(),
                                null,
                                new TestDispatcher(),
                                decoder,
                                new DefaultNodeRepository()));
    }

    @Test
    void testConstructorWithNullDispatcher() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger(
                                List.of(),
                                new MockBlockInteractor(),
                                null,
                                decoder,
                                new DefaultNodeRepository()));
    }

    @Test
    void testConstructorWithNullDecoder() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger(
                                List.of(),
                                new MockBlockInteractor(),
                                new TestDispatcher(),
                                null,
                                new DefaultNodeRepository()));
    }

    @Test
    void testConstructorWithNullNodeRepository() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger(
                                List.of(),
                                new MockBlockInteractor(),
                                new TestDispatcher(),
                                decoder,
                                null));
    }

    @Test
    void testSupports() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        assertTrue(trigger.supports(createBlockEvent(UUID.randomUUID())));
    }

    @Test
    void testSupportsFalse() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        ContractEvent contractEvent =
                new ContractEvent(
                        UUID.randomUUID(),
                        new EventName("Test"),
                        Set.of(),
                        "0x",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0x",
                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1",
                        "0x",
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO);
        assertFalse(trigger.supports(contractEvent));
    }

    @Test
    void testOnExecuteDoesNotThrow() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        assertDoesNotThrow(() -> trigger.onExecute(ev -> {}));
    }

    @Test
    void testTriggerWithoutFilters() {
        MockNode node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
        assertFalse(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithInvalidFilter() {
        MockNode node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(
                                new ContractEventFilter(
                                        UUID.randomUUID(),
                                        new FilterName("test"),
                                        UUID.randomUUID(),
                                        new EventFilterSpecification(
                                                new EventName("Test"),
                                                null,
                                                Set.of(new AddressParameterDefinition(0, false))),
                                        List.of(ContractEventStatus.CONFIRMED),
                                        new NoSyncState(),
                                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1")),
                        new MockBlockInteractor(),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
        assertFalse(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithoutLogs() {
        Node node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(
                                new ContractEventFilter(
                                        node.getId(),
                                        new FilterName("test"),
                                        node.getId(),
                                        new EventFilterSpecification(
                                                new EventName("Test"),
                                                null,
                                                Set.of(new AddressParameterDefinition(0, false))),
                                        List.of(ContractEventStatus.CONFIRMED),
                                        new NoSyncState(),
                                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1")),
                        new MockBlockInteractor(),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(node.getId())));
        assertFalse(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithLogs() {
        Node node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0x0",
                        "0xabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcd",
                        BigInteger.ZERO,
                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1",
                        "0x00000000000000000000000090f8bf6a479f320ead074411a4b0e7944ea8c9c1",
                        "0x",
                        List.of(
                                "0xaa9449f2bca09a7b28319d46fd3f3b58a1bb7d94039fc4b69b7bfe5d8535d527"));
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(
                                new ContractEventFilter(
                                        node.getId(),
                                        new FilterName("test"),
                                        node.getId(),
                                        new EventFilterSpecification(
                                                new EventName("Test"),
                                                null,
                                                Set.of(new AddressParameterDefinition(0, false))),
                                        List.of(
                                                ContractEventStatus.UNCONFIRMED,
                                                ContractEventStatus.CONFIRMED),
                                        new NoSyncState(),
                                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1")),
                        new MockBlockInteractor(log),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(node.getId())));
        assertTrue(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithLogsButHavingGlobalFilter() {
        Node node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0x0",
                        "0xabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcd",
                        BigInteger.ZERO,
                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1",
                        "0x00000000000000000000000090f8bf6a479f320ead074411a4b0e7944ea8c9c1",
                        "0x",
                        List.of(
                                "0xaa9449f2bca09a7b28319d46fd3f3b58a1bb7d94039fc4b69b7bfe5d8535d527"));
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(
                                new GlobalEventFilter(
                                        node.getId(),
                                        new FilterName("test"),
                                        node.getId(),
                                        new EventFilterSpecification(
                                                new EventName("Test"),
                                                null,
                                                Set.of(new AddressParameterDefinition(0, false))),
                                        List.of(
                                                ContractEventStatus.UNCONFIRMED,
                                                ContractEventStatus.CONFIRMED),
                                        new NoSyncState())),
                        new MockBlockInteractor(log),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(node.getId())));
        assertTrue(dispatcher.isDispatched());
    }

    @Test
    void testTriggerHavingConfirmationBlocks() {
        Node node = new MockNode(BigInteger.ONE);
        TestDispatcher dispatcher = new TestDispatcher();
        Log log =
                new Log(
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0x0",
                        "0xabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcd",
                        BigInteger.ZERO,
                        "0xa6c9f780caeafc2b8e83469a8b6422c22fa39ba1",
                        "0x00000000000000000000000090f8bf6a479f320ead074411a4b0e7944ea8c9c1",
                        "0x",
                        List.of(
                                "0xaa9449f2bca09a7b28319d46fd3f3b58a1bb7d94039fc4b69b7bfe5d8535d527"));
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(
                                new GlobalEventFilter(
                                        node.getId(),
                                        new FilterName("test"),
                                        node.getId(),
                                        new EventFilterSpecification(
                                                new EventName("Test"),
                                                null,
                                                Set.of(new AddressParameterDefinition(0, false))),
                                        List.of(
                                                ContractEventStatus.UNCONFIRMED,
                                                ContractEventStatus.CONFIRMED),
                                        new NoSyncState())),
                        new MockBlockInteractor(log),
                        dispatcher,
                        decoder,
                        new DefaultNodeRepository(node));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(node.getId())));
        assertTrue(dispatcher.isDispatched());
    }

    @Test
    void testCallbackInvocation() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        AtomicBoolean called = new AtomicBoolean(false);
        trigger.onExecute(ev -> called.set(true));
        trigger.trigger(createBlockEvent(UUID.randomUUID()));
        assertTrue(called.get(), "Consumer should be invoked on trigger");
    }

    @Test
    void testCallbackExceptionIsHandled() {
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger(
                        List.of(),
                        new MockBlockInteractor(),
                        new TestDispatcher(),
                        decoder,
                        new DefaultNodeRepository());
        trigger.onExecute(
                ev -> {
                    throw new RuntimeException("fail");
                });
        // Should not propagate exception
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
    }
}
