package io.naryo.application.node.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.application.node.trigger.disposable.block.TransactionEventConfirmationDisposableTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransactionEventDispatcherHelper {

    private final Dispatcher dispatcher;
    private final BlockInteractor blockInteractor;

    public TransactionEventDispatcherHelper(
            Dispatcher dispatcher, BlockInteractor blockInteractor) {
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(blockInteractor, "blockInteractor cannot be null");
        this.dispatcher = dispatcher;
        this.blockInteractor = blockInteractor;
    }

    public void execute(Node node, TransactionFilter filter, TransactionEvent transactionEvent) {
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) node.getSubscriptionConfiguration();

        if (filter.getStatuses().contains(TransactionStatus.FAILED)
                && transactionEvent.getStatus().equals(TransactionStatus.FAILED)) {
            try {
                transactionEvent.setRevertReason(
                        blockInteractor.getRevertReason(transactionEvent.getHash()));
            } catch (IOException e) {
                log.error(
                        "Failed to get revert reason for transaction {}",
                        transactionEvent.getHash(),
                        e);
            }
            dispatcher.dispatch(transactionEvent);
            return;
        }

        if (configuration
                        .getConfirmationBlocks()
                        .isGreaterThan(new NonNegativeBlockNumber(BigInteger.ZERO))
                && filter.getStatuses().contains(TransactionStatus.CONFIRMED)) {
            transactionEvent.setStatus(TransactionStatus.UNCONFIRMED);

            log.debug("Adding confirmation trigger for {}", transactionEvent);
            DisposableTrigger<?> trigger =
                    new TransactionEventConfirmationDisposableTrigger(
                            transactionEvent,
                            configuration.getConfirmationBlocks().value(),
                            dispatcher);
            dispatcher.addTrigger(trigger);
        }

        if (filter.getStatuses().contains(TransactionStatus.UNCONFIRMED)) {
            dispatcher.dispatch(transactionEvent);
        }
    }
}
