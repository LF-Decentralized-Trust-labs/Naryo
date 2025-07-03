package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.common.util.EncryptionUtil;
import io.naryo.application.event.decoder.block.DefaultContractEventParameterDecoder;
import io.naryo.application.filter.util.BloomFilterUtil;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockProcessorPermanentTriggerTest {

    private final DefaultContractEventParameterDecoder decoder =
            new DefaultContractEventParameterDecoder();

    private static BlockEvent createBlockEvent(UUID nodeId) {
        return createBlockEvent(nodeId, "0x0");
    }

    private static BlockEvent createBlockEvent(UUID nodeId, String logBloom) {
        return new BlockEvent(
                nodeId,
                new NonNegativeBlockNumber(BigInteger.ZERO),
                "0x0",
                logBloom,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                List.of(
                        new Transaction(
                                "0x0",
                                BigInteger.ONE,
                                BigInteger.ZERO,
                                BigInteger.TEN,
                                "0x0",
                                "0x0",
                                "0x0",
                                "0x0",
                                "0x0",
                                "0x0",
                                "0x0",
                                "0x0")));
    }

    @Test
    void testConstructor() {
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
        assertNotNull(trigger);
    }

    @Test
    void testConstructorWithNullFilters() {
        BlockInteractor interactor = new MockBlockInteractor();
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger<>(
                                new MockNode(),
                                null,
                                interactor,
                                decoder,
                                new ContractEventDispatcherHelper(
                                        new TestDispatcher(), interactor)));
    }

    @Test
    void testConstructorWithNullBlockInteractor() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger<>(
                                new MockNode(),
                                List.of(),
                                null,
                                decoder,
                                new ContractEventDispatcherHelper(new TestDispatcher(), null)));
    }

    @Test
    void testConstructorWithNullHelper() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger<>(
                                new MockNode(),
                                List.of(),
                                new MockBlockInteractor(),
                                decoder,
                                null));
    }

    @Test
    void testConstructorWithNullDecoder() {
        BlockInteractor interactor = new MockBlockInteractor();
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger<>(
                                new MockNode(),
                                List.of(),
                                interactor,
                                null,
                                new ContractEventDispatcherHelper(
                                        new TestDispatcher(), interactor)));
    }

    @Test
    void testConstructorWithNullNode() {
        BlockInteractor interactor = new MockBlockInteractor();
        assertThrows(
                NullPointerException.class,
                () ->
                        new BlockProcessorPermanentTrigger<>(
                                null,
                                List.of(),
                                interactor,
                                decoder,
                                new ContractEventDispatcherHelper(
                                        new TestDispatcher(), interactor)));
    }

    @Test
    void testSupports() {
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        new MockBlockInteractor(),
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
        assertTrue(trigger.supports(createBlockEvent(UUID.randomUUID())));
    }

    @Test
    void testSupportsFalse() {
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
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
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger<MockNode, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
        assertDoesNotThrow(() -> trigger.onExecute(ev -> {}));
    }

    @Test
    void testTriggerWithoutFilters() {
        MockNode node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger<MockNode, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
        assertFalse(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithInvalidFilter() {
        MockNode node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger<MockNode, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
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
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
        assertFalse(dispatcher.isDispatched());
    }

    @Test
    void testTriggerWithoutLogs() {
        Node node = new MockNode();
        TestDispatcher dispatcher = new TestDispatcher();
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger<Node, MockBlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
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
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
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
        BlockInteractor interactor = new MockBlockInteractor(log);
        var filter =
                new GlobalEventFilter(
                        node.getId(),
                        new FilterName("test"),
                        node.getId(),
                        new EventFilterSpecification(
                                new EventName("Test"),
                                null,
                                Set.of(new AddressParameterDefinition(0, false))),
                        List.of(ContractEventStatus.UNCONFIRMED, ContractEventStatus.CONFIRMED),
                        new NoSyncState());
        BlockProcessorPermanentTrigger<Node, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
                        List.of(filter),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
        assertDoesNotThrow(
                () -> trigger.trigger(createBlockEvent(node.getId(), generateBloom(filter))));
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
        BlockInteractor interactor = new MockBlockInteractor(log);
        var filter =
                new GlobalEventFilter(
                        node.getId(),
                        new FilterName("test"),
                        node.getId(),
                        new EventFilterSpecification(
                                new EventName("Test"),
                                null,
                                Set.of(new AddressParameterDefinition(0, false))),
                        List.of(ContractEventStatus.UNCONFIRMED, ContractEventStatus.CONFIRMED),
                        new NoSyncState());
        BlockProcessorPermanentTrigger<Node, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
                        List.of(filter),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
        assertDoesNotThrow(
                () -> trigger.trigger(createBlockEvent(node.getId(), generateBloom(filter))));
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
        BlockInteractor interactor = new MockBlockInteractor(log);
        var filter =
                new GlobalEventFilter(
                        node.getId(),
                        new FilterName("test"),
                        node.getId(),
                        new EventFilterSpecification(
                                new EventName("Test"),
                                null,
                                Set.of(new AddressParameterDefinition(0, false))),
                        List.of(ContractEventStatus.UNCONFIRMED, ContractEventStatus.CONFIRMED),
                        new NoSyncState());
        BlockProcessorPermanentTrigger<Node, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        node,
                        List.of(filter),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(dispatcher, interactor));
        assertDoesNotThrow(
                () -> trigger.trigger(createBlockEvent(node.getId(), generateBloom(filter))));
        assertTrue(dispatcher.isDispatched());
    }

    @Test
    void testCallbackInvocation() {
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger<MockNode, BlockInteractor> trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
        AtomicBoolean called = new AtomicBoolean(false);
        trigger.onExecute(ev -> called.set(true));
        trigger.trigger(createBlockEvent(UUID.randomUUID()));
        assertTrue(called.get(), "Consumer should be invoked on trigger");
    }

    @Test
    void testCallbackExceptionIsHandled() {
        BlockInteractor interactor = new MockBlockInteractor();
        BlockProcessorPermanentTrigger trigger =
                new BlockProcessorPermanentTrigger<>(
                        new MockNode(),
                        List.of(),
                        interactor,
                        decoder,
                        new ContractEventDispatcherHelper(new TestDispatcher(), interactor));
        trigger.onExecute(
                ev -> {
                    throw new RuntimeException("fail");
                });
        // Should not propagate exception
        assertDoesNotThrow(() -> trigger.trigger(createBlockEvent(UUID.randomUUID())));
    }

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
                            BigInteger.ZERO,
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

        @Override
        public boolean supportsContractAddressInBloom() {
            return false;
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
        public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, List<String> topics) {
            return List.of();
        }

        @Override
        public List<Log> getLogs(
                BigInteger startBlock, BigInteger endBlock, String contractAddress) {
            return List.of();
        }

        @Override
        public List<Log> getLogs(
                BigInteger startBlock,
                BigInteger endBlock,
                String contractAddress,
                List<String> topics) {
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

        @Override
        public TransactionReceipt getTransactionReceipt(String transactionHash) {
            return null;
        }

        @Override
        public String getRevertReason(String transactionHash) throws IOException {
            return "";
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

    private String generateBloom(GlobalEventFilter filter) {
        return EncryptionUtil.hexlify(
                BloomFilterUtil.buildBloom(
                        EncryptionUtil.sha3String(filter.getSpecification().getEventSignature())));
    }
}
