package io.naryo.application.node.trigger.disposable.block;

import java.math.BigInteger;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransactionEventConfirmationDisposableTrigger
        implements DisposableTrigger<BlockEvent> {

    private final TransactionEvent transactionEvent;
    private final BigInteger targetBlock;
    private final Dispatcher dispatcher;
    private Consumer<BlockEvent> consumer;

    public TransactionEventConfirmationDisposableTrigger(
            TransactionEvent transactionEvent,
            BigInteger confirmationBlocks,
            Dispatcher dispatcher) {
        this.transactionEvent = transactionEvent;
        this.dispatcher = dispatcher;
        this.targetBlock =
                BigInteger.ZERO
                        .add(transactionEvent.getBlockNumber().value())
                        .add(confirmationBlocks);
    }

    @Override
    public void onDispose(Consumer<BlockEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(BlockEvent event) {
        if (targetBlock.equals(event.getNumber().value())) {
            transactionEvent.setStatus(TransactionStatus.CONFIRMED);
            dispatcher.dispatch(transactionEvent);
            callback(event);
        }
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof BlockEvent;
    }

    private void callback(BlockEvent event) {
        try {
            consumer.accept(event);
        } catch (Exception e) {
            log.error("Error while processing internal consumer", e);
        }
    }
}
