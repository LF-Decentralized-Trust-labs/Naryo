package io.naryo.application.node.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Objects;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.application.node.trigger.disposable.block.TransactionEventConfirmationDisposableTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.infrastructure.node.interactor.hedera.HederaMirrorNodeBlockInteractor;
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

        // TODO: Decouple this to be protocol-agnostic
        if (transactionEvent instanceof EthTransactionEvent ethTransactionEvent) {
            executeEthTransactionEvent(filter, ethTransactionEvent);
            return;
        }

        // TODO: Decouple this to be protocol-agnostic
        if (transactionEvent instanceof HederaTransactionEvent hederaTransactionEvent) {
            executeHederaTransactionEvent(filter, hederaTransactionEvent);
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

    private void executeHederaTransactionEvent(
            TransactionFilter filter, HederaTransactionEvent transactionEvent) {
        if (transactionEvent.getName().equals(TransactionName.CONSENSUSSUBMITMESSAGE)
                && blockInteractor instanceof HederaMirrorNodeBlockInteractor interactor) {
            try {
                var message = interactor.getMessage(transactionEvent.getConsensusTimestamp());
                if (message.topicId().equals(filter.getValue())) {
                    var decodedMessage = Base64.getDecoder().decode(message.message());
                    transactionEvent.setMessage(new String(decodedMessage));
                    dispatcher.dispatch(transactionEvent);
                }
            } catch (IOException e) {
                log.error(
                        "Failed to get message for transaction {}",
                        transactionEvent.getTransactionId(),
                        e);
            }
        }
    }

    public void executeEthTransactionEvent(
            TransactionFilter filter, EthTransactionEvent transactionEvent) {
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
        }
    }
}
