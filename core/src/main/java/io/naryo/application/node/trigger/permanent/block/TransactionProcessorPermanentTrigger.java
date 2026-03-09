package io.naryo.application.node.trigger.permanent.block;

import java.util.stream.Stream;

import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionProcessorPermanentTrigger<N extends Node>
        implements PermanentTrigger<BlockEvent> {

    protected final N node;
    protected final LiveRegistry<Filter> filters;
    protected final TransactionEventDispatcherHelper helper;
    protected Consumer<BlockEvent> consumer;

    public TransactionProcessorPermanentTrigger(
            N node, LiveRegistry<Filter> filters, TransactionEventDispatcherHelper helper) {
        this.node = node;
        this.filters = filters;
        this.helper = helper;
    }

    @Override
    public boolean supports(Event<?> event) {
        return event instanceof BlockEvent && event.getNodeId().equals(node.getId());
    }

    @Override
    public void onExecute(Consumer<BlockEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(BlockEvent event) {
        event.getTransactions().forEach(tx -> processTransaction(tx, event));
    }

    protected void processTransaction(Transaction transaction, BlockEvent block) {
        findTransactionFilters()
                .filter(filter -> filter.matches(transaction))
                .forEach(
                        filter -> {
                            log.debug("Found filter {} for transaction {}", filter, transaction);
                            helper.execute(
                                    node, filter, extractEventFromTransaction(transaction, block));
                            callback(block);
                        });
    }

    protected TransactionEvent extractEventFromTransaction(
            Transaction transaction, BlockEvent block) {
        // TODO: Decouple this to be protocol-agnostic
        if (transaction instanceof EthTransaction ethTransaction) {
            return getEthTransactionEvent(block, ethTransaction);
        } else if (transaction instanceof HederaTransaction hederaTransaction) {
            return getHederaTransactionEvent(block, hederaTransaction);
        }
        throw new UnsupportedOperationException("Unsupported transaction type: " + transaction);
    }

    protected void callback(BlockEvent event) {
        if (consumer != null) {
            try {
                consumer.accept(event);
            } catch (Exception e) {
                log.error("Error calling consumer for block event {}", event, e);
            }
        } else {
            log.debug("No consumer found for block event {}", event);
        }
    }

    protected Stream<TransactionFilter> findTransactionFilters() {
        return filters.active().domainItems().stream()
                .filter(
                        f ->
                                f.getType() == FilterType.TRANSACTION
                                        && f.getNodeId().equals(node.getId()))
                .map(TransactionFilter.class::cast);
    }

    private EthTransactionEvent getEthTransactionEvent(
            BlockEvent block, EthTransaction ethTransaction) {
        return new EthTransactionEvent(
                node.getId(),
                ethTransaction.getHash(),
                ethTransaction.getStatus() == null || ethTransaction.getStatus().equals("0x1")
                        ? TransactionStatus.CONFIRMED
                        : TransactionStatus.FAILED,
                new NonNegativeBlockNumber(ethTransaction.getBlockNumber()),
                block.getTimestamp(),
                ethTransaction.getFrom(),
                ethTransaction.getTo(),
                ethTransaction.getValue(),
                ethTransaction.getBlockHash(),
                new NonNegativeBlockNumber(ethTransaction.getNonce()),
                ethTransaction.getIndex(),
                ethTransaction.getInput(),
                ethTransaction.getRevertReason());
    }

    private HederaTransactionEvent getHederaTransactionEvent(
            BlockEvent block, HederaTransaction hederaTransaction) {
        return new HederaTransactionEvent(
                node.getId(),
                hederaTransaction.getHash(),
                hederaTransaction.getStatus() == null || hederaTransaction.getStatus().equals("0x1")
                        ? TransactionStatus.CONFIRMED
                        : TransactionStatus.FAILED,
                new NonNegativeBlockNumber(hederaTransaction.getBlockNumber()),
                block.getTimestamp(),
                hederaTransaction.getFrom(),
                hederaTransaction.getTo(),
                hederaTransaction.getValue(),
                hederaTransaction.getBatchKey(),
                hederaTransaction.getBytes(),
                hederaTransaction.getChargedTxFee(),
                hederaTransaction.getEntityId(),
                hederaTransaction.getMaxCustomFees(),
                hederaTransaction.getMemoBase64(),
                hederaTransaction.getName(),
                hederaTransaction.getNftTransfers(),
                hederaTransaction.getNode(),
                hederaTransaction.getNonce(),
                hederaTransaction.getParentConsensusTimestamp(),
                hederaTransaction.getScheduled(),
                hederaTransaction.getStakingRewardTransfers(),
                hederaTransaction.getTokenTransfers(),
                hederaTransaction.getTransactionId(),
                hederaTransaction.getTransfers(),
                hederaTransaction.getValidDurationSeconds(),
                hederaTransaction.getValidStartTimestamp(),
                hederaTransaction.getConsensusTimestamp(),
                null);
    }
}
