package io.naryo.application.node.trigger.disposable.block;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ContractEventConfirmationDisposableTrigger
        implements DisposableTrigger<BlockEvent> {

    private final ContractEvent contractEvent;
    private final BigInteger targetBlock;
    private final Dispatcher dispatcher;
    private final BlockInteractor blockInteractor;
    private final NonNegativeBlockNumber missingTxRetryBlocks;
    private final NonNegativeBlockNumber eventInvalidationBlockThreshold;
    private Consumer<BlockEvent> consumer;
    private NonNegativeBlockNumber missingTxBlockLimit;
    private NonNegativeBlockNumber currentNumBlocksToWaitBeforeInvalidating;
    private boolean isInvalidated;

    public ContractEventConfirmationDisposableTrigger(
            ContractEvent contractEvent,
            NonNegativeBlockNumber confirmationBlocks,
            NonNegativeBlockNumber missingTxRetryBlocks,
            NonNegativeBlockNumber eventInvalidationBlockThreshold,
            Dispatcher dispatcher,
            BlockInteractor blockInteractor) {
        Objects.requireNonNull(contractEvent, "contractEvent must not be null");
        Objects.requireNonNull(confirmationBlocks, "Confirmation blocks must not be null");
        Objects.requireNonNull(dispatcher, "Dispatcher must not be null");
        Objects.requireNonNull(blockInteractor, "Block interactor must not be null");
        Objects.requireNonNull(missingTxRetryBlocks, "Missing tx retry blocks must not be null");
        Objects.requireNonNull(
                eventInvalidationBlockThreshold,
                "Event invalidation block threshold must not be null");
        this.contractEvent = contractEvent;
        this.dispatcher = dispatcher;
        this.blockInteractor = blockInteractor;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.targetBlock =
                BigInteger.ZERO.add(contractEvent.getBlockNumber()).add(confirmationBlocks.value());
    }

    @Override
    public void onDispose(Consumer<BlockEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(BlockEvent block) {
        try {
            final TransactionReceipt transaction =
                    blockInteractor.getTransactionReceipt(contractEvent.getTransactionHash());

            if (transaction == null) {
                // Tx has disappeared...we've probably forked
                // Tx should be included in block on new fork soon
                handleMissingTransaction(block);
                return;
            }

            checkEventStatus(block, transaction);
        } catch (IOException e) {
            // TODO: Handle the error
            log.error(
                    "Error occurred while confirming the event {} for block {}.",
                    contractEvent.getName(),
                    block.getNumber().value(),
                    e);
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

    private void checkEventStatus(BlockEvent block, TransactionReceipt transaction) {
        if (isInvalidated) return;
        if (isOrphaned(transaction)) {
            processInvalidatedEvent(block);
            return;
        }
        processConfirmedEvent(block);
    }

    private void processConfirmedEvent(BlockEvent block) {
        if (!targetBlock.equals(block.getNumber().value())) {
            return;
        }
        contractEvent.setStatus(ContractEventStatus.CONFIRMED);
        dispatcher.dispatch(contractEvent);
        callback(block);
    }

    private void handleMissingTransaction(BlockEvent block) {
        if (missingTxBlockLimit == null) {
            missingTxBlockLimit = block.getNumber().add(missingTxRetryBlocks);
        } else if (block.getNumber().compareTo(missingTxBlockLimit) > 0) {
            invalidateEvent(block);
        }
    }

    private boolean isOrphaned(TransactionReceipt transaction) {
        // If block hash is not as expected, this means that the transaction
        // has been included in a block on a different fork of a longer chain
        // and the original transaction is considered orphaned.
        String orphanReason = null;

        if (!transaction.blockHash().equals(contractEvent.getBlockHash())) {
            orphanReason =
                    String.format(
                            "Expected block hash %s, received %s",
                            contractEvent.getBlockHash(), transaction.blockHash());
        }

        if (orphanReason != null) {
            log.info("Orphan event detected: {}", orphanReason);
            return true;
        }

        return false;
    }

    private void processInvalidatedEvent(BlockEvent block) {
        if (currentNumBlocksToWaitBeforeInvalidating == null) {
            currentNumBlocksToWaitBeforeInvalidating =
                    block.getNumber().add(eventInvalidationBlockThreshold);
        } else if (block.getNumber().compareTo(currentNumBlocksToWaitBeforeInvalidating) > 0) {
            invalidateEvent(block);
        }
    }

    private void invalidateEvent(BlockEvent block) {
        isInvalidated = true;
        contractEvent.setStatus(ContractEventStatus.INVALIDATED);
        dispatcher.dispatch(contractEvent);
        callback(block);
    }
}
