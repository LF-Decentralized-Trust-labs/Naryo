package io.librevents.application.node.dispatch.block;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.librevents.application.node.trigger.Trigger;
import io.librevents.application.node.trigger.disposable.DisposableTrigger;
import io.librevents.application.node.trigger.permanent.PermanentTrigger;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.transaction.TransferTransactionEvent;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockDispatcherTest {

    private static class MockDisposableTrigger
            implements DisposableTrigger<TransferTransactionEvent> {

        private Consumer<TransferTransactionEvent> consumer;

        @Override
        public void trigger(TransferTransactionEvent event) {
            // Mock trigger logic
            System.out.println("Triggering event: " + event);

            if (consumer != null) {
                try {
                    consumer.accept(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean supports(Event event) {
            return event instanceof TransferTransactionEvent;
        }

        @Override
        public void onDispose(Consumer<TransferTransactionEvent> consumer) {
            this.consumer = consumer;
        }
    }

    private static class MockPermanentTrigger
            implements PermanentTrigger<TransferTransactionEvent> {

        private Consumer<TransferTransactionEvent> consumer;

        @Override
        public void trigger(TransferTransactionEvent event) {
            // Mock trigger logic
            System.out.println("Triggering event: " + event);

            if (consumer != null) {
                try {
                    consumer.accept(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean supports(Event event) {
            return event instanceof TransferTransactionEvent;
        }

        @Override
        public void onExecute(Consumer<TransferTransactionEvent> consumer) {
            this.consumer = consumer;
        }
    }

    private static class InvalidMockTrigger implements DisposableTrigger<BlockEvent> {

        @Override
        public void trigger(BlockEvent event) {
            // Mock trigger logic
            System.out.println("Triggering event: " + event);
        }

        @Override
        public boolean supports(Event event) {
            return false;
        }

        @Override
        public void onDispose(Consumer<BlockEvent> consumer) {
            // No-op
        }
    }

    @Test
    void dispatchWithoutTriggers() {
        BlockDispatcher blockDispatcher = new BlockDispatcher(Set.of());
        assertDoesNotThrow(
                () ->
                        blockDispatcher.dispatch(
                                new TransferTransactionEvent(
                                        UUID.randomUUID(),
                                        "0x1234567890abcdef",
                                        TransactionStatus.FAILED,
                                        new NonNegativeBlockNumber(BigInteger.ZERO),
                                        "0xabcdef1234567890",
                                        new NonNegativeBlockNumber(BigInteger.ONE),
                                        BigInteger.TEN,
                                        BigInteger.ZERO,
                                        "0xabcdef1234567890",
                                        "0x1234567890abcdef",
                                        "0xabcdef1234567890",
                                        "0x1234567890abcdef",
                                        "0xabcdef1234567890")));
    }

    @Test
    void dispatchWithTriggers() {
        BlockDispatcher blockDispatcher = new BlockDispatcher(Set.of(
                    new MockDisposableTrigger(),
                    new MockPermanentTrigger(),
                    new InvalidMockTrigger()));
        assertDoesNotThrow(
                () ->
                        blockDispatcher.dispatch(
                                new TransferTransactionEvent(
                                        UUID.randomUUID(),
                                        "0x1234567890abcdef",
                                        TransactionStatus.FAILED,
                                        new NonNegativeBlockNumber(BigInteger.ZERO),
                                        "0xabcdef1234567890",
                                        new NonNegativeBlockNumber(BigInteger.ONE),
                                        BigInteger.TEN,
                                        BigInteger.ZERO,
                                        "0xabcdef1234567890",
                                        "0x1234567890abcdef",
                                        "0xabcdef1234567890",
                                        "0x1234567890abcdef",
                                        "0xabcdef1234567890")));
    }

    @Test
    void addTrigger() {
        BlockDispatcher blockDispatcher = new BlockDispatcher(Set.of());
        MockDisposableTrigger trigger = new MockDisposableTrigger();
        blockDispatcher.addTrigger(trigger);
        assertTrue(blockDispatcher.getTriggers().contains(trigger));
    }

    @Test
    void removeTrigger() {
        BlockDispatcher blockDispatcher = new BlockDispatcher(Set.of());
        MockDisposableTrigger trigger = new MockDisposableTrigger();
        blockDispatcher.addTrigger(trigger);
        blockDispatcher.removeTrigger(trigger);
        assertFalse(blockDispatcher.getTriggers().contains(trigger));
    }
}
