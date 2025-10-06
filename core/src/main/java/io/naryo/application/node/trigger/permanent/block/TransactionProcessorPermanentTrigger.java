package io.naryo.application.node.trigger.permanent.block;

import java.util.stream.Stream;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
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
    protected final LiveView<Filter> filters;
    protected final TransactionEventDispatcherHelper helper;
    protected Consumer<BlockEvent> consumer;

    public TransactionProcessorPermanentTrigger(
            N node, LiveView<Filter> filters, TransactionEventDispatcherHelper helper) {
        this.node = node;
        this.filters = filters;
        this.helper = helper;
    }

    @Override
    public boolean supports(Event<?> event) {
        return event instanceof BlockEvent;
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
        return new TransactionEvent(
                node.getId(),
                transaction.hash(),
                transaction.status() == null || transaction.status().equals("0x1")
                        ? TransactionStatus.CONFIRMED
                        : TransactionStatus.FAILED,
                new NonNegativeBlockNumber(transaction.nonce()),
                transaction.blockHash(),
                new NonNegativeBlockNumber(transaction.blockNumber()),
                block.getTimestamp(),
                transaction.index(),
                transaction.from(),
                transaction.to(),
                transaction.value(),
                transaction.input(),
                transaction.revertReason());
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
        return filters.revision().domainItems().stream()
                .filter(f -> f.getType() == FilterType.TRANSACTION && f.getNodeId() == node.getId())
                .map(TransactionFilter.class::cast);
    }
}
