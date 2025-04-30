package io.librevents.application.node.trigger.disposable.block;

import java.math.BigInteger;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.transaction.TransactionEvent;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionEventConfirmationDisposableTriggerTest {

    @Mock private TransactionEvent transactionEvent;

    @Mock private Dispatcher dispatcher;

    @Mock private Consumer<BlockEvent> consumer;

    private final BigInteger initialBlock = BigInteger.valueOf(100);
    private final BigInteger confirmations = BigInteger.valueOf(5);
    private TransactionEventConfirmationDisposableTrigger trigger;

    @BeforeEach
    void setUp() {
        when(transactionEvent.getBlockNumber())
                .thenReturn(new NonNegativeBlockNumber(initialBlock));
        trigger =
                new TransactionEventConfirmationDisposableTrigger(
                        transactionEvent, confirmations, dispatcher);
        trigger.onDispose(consumer);
    }

    @Test
    void supports_onlyBlockEvent() {
        BlockEvent blockEvent = mock(BlockEvent.class);
        Event otherEvent = mock(Event.class);

        assertTrue(trigger.supports(blockEvent));
        assertFalse(trigger.supports(otherEvent));
    }

    @Test
    void trigger_atTarget_dispatchesAndConfirmsAndCallbacks() throws Exception {
        BigInteger target = initialBlock.add(confirmations);
        BlockEvent blockEvent = mock(BlockEvent.class);
        when(blockEvent.getNumber()).thenReturn(new NonNegativeBlockNumber(target));

        trigger.trigger(blockEvent);

        verify(transactionEvent).setStatus(TransactionStatus.CONFIRMED);
        verify(dispatcher).dispatch(transactionEvent);
        verify(consumer).accept(blockEvent);
    }

    @Test
    void trigger_beforeTarget_noDispatchOrCallback() throws Exception {
        BigInteger before = initialBlock.add(confirmations).subtract(BigInteger.ONE);
        BlockEvent blockEvent = mock(BlockEvent.class);
        when(blockEvent.getNumber()).thenReturn(new NonNegativeBlockNumber(before));

        trigger.trigger(blockEvent);

        verify(transactionEvent, never()).setStatus(any());
        verify(dispatcher, never()).dispatch(any());
        verify(consumer, never()).accept(any());
    }

    @Test
    void trigger_callbackException_caught_andDispatchStillOccurs() throws Exception {
        BigInteger target = initialBlock.add(confirmations);
        BlockEvent blockEvent = mock(BlockEvent.class);
        when(blockEvent.getNumber()).thenReturn(new NonNegativeBlockNumber(target));
        doThrow(new RuntimeException("callback failure")).when(consumer).accept(blockEvent);

        assertDoesNotThrow(() -> trigger.trigger(blockEvent));

        verify(transactionEvent).setStatus(TransactionStatus.CONFIRMED);
        verify(dispatcher).dispatch(transactionEvent);
        verify(consumer).accept(blockEvent);
    }

    @Test
    void onDispose_registrationDoesNotThrow() {
        assertDoesNotThrow(() -> trigger.onDispose(evt -> {}));
    }
}
