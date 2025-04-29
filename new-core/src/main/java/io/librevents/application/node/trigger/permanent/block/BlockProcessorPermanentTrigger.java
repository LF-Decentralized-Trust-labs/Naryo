package io.librevents.application.node.trigger.permanent.block;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import io.librevents.application.common.util.EncryptionUtil;
import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.filter.util.BloomFilterUtil;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.application.node.trigger.disposable.DisposableTrigger;
import io.librevents.application.node.trigger.disposable.block.ContractEventConfirmationDisposableTrigger;
import io.librevents.application.node.trigger.permanent.PermanentTrigger;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.filter.event.ContractEventFilter;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.GlobalEventFilter;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeRepository;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BlockProcessorPermanentTrigger implements PermanentTrigger<BlockEvent> {

    private final List<EventFilter> filters;
    private final BlockInteractor interactor;
    private final Dispatcher dispatcher;
    private final ContractEventParameterDecoder decoder;
    private final NodeRepository nodeRepository;
    private Consumer<BlockEvent> consumer;

    public BlockProcessorPermanentTrigger(
            List<EventFilter> filters,
            BlockInteractor interactor,
            Dispatcher dispatcher,
            ContractEventParameterDecoder decoder,
            NodeRepository nodeRepository) {
        Objects.requireNonNull(filters, "filters cannot be null");
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(decoder, "decoder cannot be null");
        Objects.requireNonNull(nodeRepository, "nodeRepository cannot be null");
        this.filters = filters;
        this.interactor = interactor;
        this.dispatcher = dispatcher;
        this.decoder = decoder;
        this.nodeRepository = nodeRepository;
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
        processBlock(event);
        callback(event);
    }

    private void processBlock(BlockEvent event) {
        Optional<Node> nodeOptional = nodeRepository.findById(event.getNodeId());
        if (nodeOptional.isEmpty()) {
            log.debug("Node not found for block event {}", event);
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
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) nodeOptional.get().getSubscriptionConfiguration();
        for (EventFilter filter : foundFilters) {
            Predicate<Log> predicate = getLogPredicate(filter);
            logs.stream()
                    .filter(predicate)
                    .forEach(
                            value -> {
                                log.debug("Found log {} for filter {}", value, filter);
                                Transaction transaction =
                                        event.getTransactions().stream()
                                                .filter(
                                                        tx ->
                                                                tx.hash()
                                                                        .equals(
                                                                                value
                                                                                        .transactionHash()))
                                                .findFirst()
                                                .orElse(null);
                                ContractEvent contractEvent =
                                        new ContractEvent(
                                                event.getNodeId(),
                                                filter.getSpecification().eventName(),
                                                decoder.decode(
                                                        filter.getSpecification(), value.data()),
                                                value.transactionHash(),
                                                value.index(),
                                                event.getNumber().value(),
                                                event.getHash(),
                                                value.address(),
                                                transaction != null ? transaction.from() : null,
                                                ContractEventStatus.CONFIRMED,
                                                event.getTimestamp());

                                if (configuration
                                                .getConfirmationBlocks()
                                                .isGreaterThan(
                                                        new NonNegativeBlockNumber(BigInteger.ZERO))
                                        && filter.getStatuses()
                                                .contains(ContractEventStatus.CONFIRMED)) {
                                    contractEvent.setStatus(ContractEventStatus.UNCONFIRMED);

                                    log.debug("Adding confirmation trigger for {}", contractEvent);
                                    DisposableTrigger<?> trigger =
                                            new ContractEventConfirmationDisposableTrigger(
                                                    contractEvent,
                                                    configuration.getConfirmationBlocks().value(),
                                                    dispatcher);
                                    dispatcher.addTrigger(trigger);
                                }

                                if (filter.getStatuses()
                                        .contains(ContractEventStatus.UNCONFIRMED)) {
                                    dispatcher.dispatch(contractEvent);
                                }
                            });
        }
    }

    private void callback(BlockEvent event) {
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

    private List<EventFilter> findFilters(BlockEvent event) {
        return filters.stream()
                .filter(
                        filter -> {
                            if (!filter.getNodeId().equals(event.getNodeId())) {
                                return false;
                            }

                            if (filter instanceof ContractEventFilter contractFilter) {
                                return BloomFilterUtil.bloomFilterMatch(
                                        event.getLogsBloom(),
                                        BloomFilterUtil.getBloomBitsForFilter(
                                                contractFilter.getContractAddress(),
                                                filter.getSpecification().getEventSignature()));
                            }

                            if (filter instanceof GlobalEventFilter) {
                                return BloomFilterUtil.bloomFilterMatch(
                                        event.getLogsBloom(),
                                        filter.getSpecification().getEventSignature());
                            }

                            return false;
                        })
                .toList();
    }

    private static Predicate<Log> getLogPredicate(EventFilter filter) {
        if (filter instanceof ContractEventFilter contractFilter) {
            return log ->
                    log.address().equals(contractFilter.getContractAddress())
                            && log.topics()
                                    .contains(
                                            EncryptionUtil.keccak256Hex(
                                                    filter.getSpecification().getEventSignature()));
        }
        return log ->
                log.topics()
                        .contains(
                                EncryptionUtil.keccak256Hex(
                                        filter.getSpecification().getEventSignature()));
    }
}
