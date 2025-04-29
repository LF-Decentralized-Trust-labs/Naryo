package io.librevents.application.node.trigger.disposable.block;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.common.EventName;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.contract.ContractEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractEventConfirmationDisposableTriggerTest {

    private static Event eventReceived;

    private static class MockDispatcher implements Dispatcher {

        @Override
        public void addTrigger(Trigger<?> trigger) {}

        @Override
        public void removeTrigger(Trigger<?> trigger) {}

        @Override
        public void dispatch(Event event) {
            eventReceived = event;
        }
    }

    @Test
    void testTrigger() {
        Dispatcher dispatcher = new MockDispatcher();
        ContractEvent contractEvent =
                new ContractEvent(
                        UUID.randomUUID(),
                        new EventName("test"),
                        Set.of(),
                        "0x1234567890abcdef",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO);
        BlockEvent blockEvent =
                new BlockEvent(
                        UUID.randomUUID(),
                        new NonNegativeBlockNumber(BigInteger.valueOf(5)),
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        List.of());

        ContractEventConfirmationDisposableTrigger trigger =
                new ContractEventConfirmationDisposableTrigger(
                        contractEvent, BigInteger.valueOf(5), dispatcher);

        assertDoesNotThrow(() -> trigger.trigger(blockEvent));
        assertTrue(eventReceived instanceof ContractEvent);
        assertSame(ContractEventStatus.CONFIRMED, ((ContractEvent) eventReceived).getStatus());
    }

    @Test
    void testTriggerWithoutValidBlock() {
        Dispatcher dispatcher = new MockDispatcher();
        ContractEvent contractEvent =
                new ContractEvent(
                        UUID.randomUUID(),
                        new EventName("test"),
                        Set.of(),
                        "0x1234567890abcdef",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO);
        BlockEvent blockEvent =
                new BlockEvent(
                        UUID.randomUUID(),
                        new NonNegativeBlockNumber(BigInteger.valueOf(3)),
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        List.of());

        ContractEventConfirmationDisposableTrigger trigger =
                new ContractEventConfirmationDisposableTrigger(
                        contractEvent, BigInteger.valueOf(5), dispatcher);

        assertDoesNotThrow(() -> trigger.trigger(blockEvent));
    }

    @Test
    void testOnDispose() {
        Dispatcher dispatcher = new MockDispatcher();
        ContractEvent contractEvent =
                new ContractEvent(
                        UUID.randomUUID(),
                        new EventName("test"),
                        Set.of(),
                        "0x1234567890abcdef",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO);

        ContractEventConfirmationDisposableTrigger trigger =
                new ContractEventConfirmationDisposableTrigger(
                        contractEvent, BigInteger.valueOf(5), dispatcher);

        assertDoesNotThrow(
                () -> {
                    trigger.onDispose(blockEvent -> {});
                });
    }

    @Test
    void testSupports() {
        Dispatcher dispatcher = new MockDispatcher();
        ContractEvent contractEvent =
                new ContractEvent(
                        UUID.randomUUID(),
                        new EventName("test"),
                        Set.of(),
                        "0x1234567890abcdef",
                        BigInteger.ZERO,
                        BigInteger.ZERO,
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        "0xabcdef1234567890",
                        ContractEventStatus.UNCONFIRMED,
                        BigInteger.ZERO);

        ContractEventConfirmationDisposableTrigger trigger =
                new ContractEventConfirmationDisposableTrigger(
                        contractEvent, BigInteger.valueOf(5), dispatcher);

        assertTrue(trigger.supports(contractEvent));
        assertFalse(
                trigger.supports(
                        new BlockEvent(
                                UUID.randomUUID(),
                                new NonNegativeBlockNumber(BigInteger.valueOf(5)),
                                "0xabcdef1234567890",
                                "0xabcdef1234567890",
                                BigInteger.ZERO,
                                BigInteger.ZERO,
                                BigInteger.ZERO,
                                BigInteger.ZERO,
                                List.of())));
    }
}
