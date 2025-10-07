package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.util.BloomFilterUtil;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PrivateEthereumBlockProcessorPermanentTrigger
        extends BlockProcessorPermanentTrigger<PrivateEthereumNode, PrivateBlockInteractor> {

    public PrivateEthereumBlockProcessorPermanentTrigger(
            PrivateEthereumNode node,
            LiveRegistry<Filter> filters,
            PrivateBlockInteractor interactor,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper) {
        super(node, filters, interactor, decoder, helper);
    }

    @Override
    protected void processBlock(BlockEvent event) throws IOException {
        List<EventFilter> foundFilters = checkIfEventMatchesNodeAndFindRelatedFilters(event);
        if (foundFilters.isEmpty()) {
            return;
        }

        List<EventFilter> publicFilters = new ArrayList<>();
        List<EventFilter> privateFilters = new ArrayList<>();

        foundFilters.forEach(
                filter -> {
                    if (filter.getVisibilityConfiguration().isVisible()) {
                        publicFilters.add(filter);
                    } else {
                        privateFilters.add(filter);
                    }
                });

        processPublicLogs(event, publicFilters);
        processPrivateLogs(event, privateFilters);
    }

    private void processPrivateLogs(BlockEvent event, List<EventFilter> privateFilters) {
        Map<String, List<Log>> logs = getPrivateLogsByPrivacyGroupId(event, privateFilters);

        if (logs.isEmpty()) {
            log.debug("No private logs found for block event {}", event);
            return;
        }

        AtomicInteger processedLogs = new AtomicInteger();

        for (EventFilter filter : privateFilters) {
            Predicate<Log> predicate = getLogPredicate(filter);

            logs.get(filter.getVisibilityConfiguration().getPrivacyGroupId()).stream()
                    .filter(predicate)
                    .forEach(
                            value -> {
                                processLog(event, filter, value);
                                processedLogs.set(+1);
                            });
        }

        log.info(
                "Processed {} logs for {} private filters for block event {}",
                processedLogs.get(),
                privateFilters.size(),
                event.getNumber().value());
    }

    private Map<String, List<Log>> getPrivateLogsByPrivacyGroupId(
            BlockEvent event, List<EventFilter> privateFilters) {
        return privateFilters.stream()
                .map(f -> f.getVisibilityConfiguration().getPrivacyGroupId())
                .distinct()
                .collect(
                        HashMap::new,
                        (map, privacyGroupId) -> {
                            try {
                                map.put(
                                        privacyGroupId,
                                        interactor.getPrivateLogs(privacyGroupId, event.getHash()));
                            } catch (IOException e) {
                                log.error(
                                        "Error getting private logs for block event {}", event, e);
                            }
                        },
                        Map::putAll);
    }

    @Override
    protected List<EventFilter> findFiltersForEvent(BlockEvent event) {
        List<EventFilter> filtersForEvent = super.findFiltersForEvent(event);
        List<String> privateLogBlooms = getPrivateLogBlooms(event);

        findActiveEventFilters()
                .filter(f -> !filtersForEvent.contains(f))
                .filter(
                        filter -> {
                            if (!filter.getNodeId().equals(event.getNodeId())) {
                                return false;
                            }

                            if (filter instanceof ContractEventFilter contractFilter) {
                                return privateLogBlooms.stream()
                                        .anyMatch(
                                                logBloom ->
                                                        BloomFilterUtil.match(
                                                                logBloom,
                                                                contractFilter.getContractAddress(),
                                                                filter.getSpecification()
                                                                        .getEventSignature()));
                            }

                            if (filter instanceof GlobalEventFilter) {
                                return privateLogBlooms.stream()
                                        .anyMatch(
                                                logBloom ->
                                                        BloomFilterUtil.match(
                                                                logBloom,
                                                                filter.getSpecification()
                                                                        .getEventSignature()));
                            }

                            return false;
                        })
                .forEach(filtersForEvent::add);

        return filtersForEvent;
    }

    private List<String> getPrivateLogBlooms(BlockEvent event) {
        return event.getTransactions().stream()
                .filter(
                        transaction ->
                                transaction.to() != null
                                        && transaction
                                                .to()
                                                .equals(node.getPrecompiledAddress().value()))
                .map(
                        tx -> {
                            try {
                                return interactor.getPrivateTransactionReceipt(tx.hash());
                            } catch (IOException e) {
                                log.error("Failed to get private transaction receipt", e);
                                return null;
                            }
                        })
                .filter(Objects::nonNull)
                .map(TransactionReceipt::logsBloom)
                .toList();
    }
}
