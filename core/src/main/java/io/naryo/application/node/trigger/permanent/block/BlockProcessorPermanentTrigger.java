package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import io.naryo.application.common.util.EncryptionUtil;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.util.BloomFilterUtil;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.node.Node;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

import static io.naryo.application.common.util.EncryptionUtil.sha3String;

@Slf4j
public class BlockProcessorPermanentTrigger<N extends Node, I extends BlockInteractor>
        implements PermanentTrigger<BlockEvent> {

    protected final N node;
    protected final List<EventFilter> filters;
    protected final I interactor;
    protected final ContractEventParameterDecoder decoder;
    protected final ContractEventDispatcherHelper helper;
    protected Consumer<BlockEvent> consumer;

    public BlockProcessorPermanentTrigger(
            N node,
            List<EventFilter> filters,
            I interactor,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper) {
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(filters, "filters cannot be null");
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(decoder, "decoder cannot be null");
        Objects.requireNonNull(helper, "helper cannot be null");
        this.node = node;
        this.filters = filters;
        this.interactor = interactor;
        this.decoder = decoder;
        this.helper = helper;
    }

    protected static Predicate<Log> getLogPredicate(EventFilter filter) {
        if (filter instanceof ContractEventFilter contractFilter) {
            return log ->
                    log.address().equalsIgnoreCase(contractFilter.getContractAddress())
                            && log.topics()
                                    .contains(
                                            sha3String(
                                                    filter.getSpecification().getEventSignature()));
        }
        return log ->
                log.topics().contains(sha3String(filter.getSpecification().getEventSignature()));
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof BlockEvent;
    }

    @Override
    public void onExecute(Consumer<BlockEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(BlockEvent event) {
        try {
            processBlock(event);
        } catch (Exception e) {
            log.error("Error processing block", e);
            // TODO: Improve error handling and block recovery
        }
        callback(event);
    }

    protected void processBlock(BlockEvent event) throws IOException {
        if (!event.getNodeId().equals(node.getId())) {
            log.debug("Skipping block event {} for node {}", event, node.getId());
            return;
        }
        List<EventFilter> foundFilters = findFilters(event);
        if (foundFilters.isEmpty()) {
            log.debug("No filters found for block event {}", event);
            return;
        }
        log.debug("Found {} filters for block event {}", foundFilters.size(), event);
        List<Log> logs = interactor.getLogs(event.getHash());
        if (logs.isEmpty()) {
            log.debug("No logs found for block event {}", event);
            return;
        }
        log.debug("Found {} logs for block event {}", logs.size(), event);

        int filterCount = 0;
        for (EventFilter filter : foundFilters) {
            Predicate<Log> predicate = getLogPredicate(filter);
            List<Log> foundLogs = logs.stream().filter(predicate).toList();
            foundLogs.forEach(value -> processLog(event, filter, value));
            filterCount += foundLogs.size();
        }
        log.info(
                "Processed {} logs for {} filters for block event {}",
                filterCount,
                foundFilters.size(),
                event.getNumber().value());
    }

    protected void processLog(BlockEvent event, EventFilter filter, Log value) {
        log.debug("Found log {} for filter {}", value, filter);
        ContractEvent contractEvent = extractEventFromLog(event, filter, value);
        helper.execute(node, filter, contractEvent);
    }

    protected ContractEvent extractEventFromLog(BlockEvent event, EventFilter filter, Log value) {
        Transaction transaction =
                event.getTransactions().stream()
                        .filter(tx -> tx.hash().equals(value.transactionHash()))
                        .findFirst()
                        .orElse(null);
        return new ContractEvent(
                event.getNodeId(),
                filter.getSpecification().eventName(),
                decoder.decode(filter.getSpecification(), value),
                value.transactionHash(),
                value.index(),
                event.getNumber().value(),
                event.getHash(),
                value.address(),
                transaction != null ? transaction.from() : null,
                ContractEventStatus.CONFIRMED,
                event.getTimestamp());
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

    protected List<EventFilter> findFilters(BlockEvent event) {
        return filters.stream()
                .filter(
                        filter -> {
                            if (!filter.getNodeId().equals(event.getNodeId())) {
                                return false;
                            }

                            String topic0 =
                                    EncryptionUtil.sha3String(
                                            filter.getSpecification().getEventSignature());
                            if (filter instanceof ContractEventFilter contractFilter
                                    && node.supportsContractAddressInBloom()) {
                                return BloomFilterUtil.match(
                                        event.getLogsBloom(),
                                        topic0,
                                        contractFilter.getContractAddress());
                            }

                            if (filter instanceof GlobalEventFilter
                                    || !node.supportsContractAddressInBloom()) {
                                return BloomFilterUtil.match(event.getLogsBloom(), topic0);
                            }

                            return false;
                        })
                .toList();
    }
}
