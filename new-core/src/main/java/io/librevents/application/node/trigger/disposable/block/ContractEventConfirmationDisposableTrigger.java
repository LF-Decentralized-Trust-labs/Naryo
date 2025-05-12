package io.librevents.application.node.trigger.disposable.block;

import java.math.BigInteger;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.trigger.disposable.DisposableTrigger;
import io.librevents.domain.common.event.ContractEventStatus;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.contract.ContractEvent;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ContractEventConfirmationDisposableTrigger
        implements DisposableTrigger<BlockEvent> {

    private final ContractEvent contractEvent;
    private final BigInteger targetBlock;
    private final Dispatcher dispatcher;
    private Consumer<BlockEvent> consumer;

    public ContractEventConfirmationDisposableTrigger(
            ContractEvent contractEvent, BigInteger confirmationBlocks, Dispatcher dispatcher) {
        this.contractEvent = contractEvent;
        this.dispatcher = dispatcher;
        this.targetBlock =
                BigInteger.ZERO.add(contractEvent.getBlockNumber()).add(confirmationBlocks);
    }

    @Override
    public void onDispose(Consumer<BlockEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(BlockEvent event) {
        if (targetBlock.equals(event.getNumber().value())) {
            contractEvent.setStatus(ContractEventStatus.CONFIRMED);
            dispatcher.dispatch(contractEvent);
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
