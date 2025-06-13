package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.util.BloomFilterUtil;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Log;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import io.naryo.domain.event.block.BlockEvent;
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
            List<EventFilter> filters,
            PrivateBlockInteractor interactor,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper) {
        super(node, filters, interactor, decoder, helper);
    }

    @Override
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
        List<Log> publicLogs = interactor.getLogs(event.getHash());
        Map<String, List<Log>> privateLogs =
                foundFilters.stream()
                        .filter(f -> !f.getVisibilityConfiguration().isVisible())
                        .map(f -> f.getVisibilityConfiguration().getPrivacyGroupId())
                        .distinct()
                        .collect(
                                HashMap::new,
                                (map, privacyGroupId) -> {
                                    try {
                                        map.put(
                                                privacyGroupId,
                                                interactor.getPrivateLogs(
                                                        privacyGroupId, event.getHash()));
                                    } catch (IOException e) {
                                        log.error(
                                                "Error getting private logs for block event {}",
                                                event,
                                                e);
                                    }
                                },
                                Map::putAll);

        if (publicLogs.isEmpty() && privateLogs.isEmpty()) {
            log.debug("No logs found for block event {}", event);
            return;
        }

        foundFilters.forEach(
                filter -> {
                    Predicate<Log> predicate = getLogPredicate(filter);
                    if (filter.getVisibilityConfiguration().isVisible()) {
                        publicLogs.stream()
                                .filter(predicate)
                                .forEach(value -> processLog(event, filter, value));
                        return;
                    }

                    privateLogs
                            .get(filter.getVisibilityConfiguration().getPrivacyGroupId())
                            .stream()
                            .filter(predicate)
                            .forEach(value -> processLog(event, filter, value));
                });
    }

    @Override
    protected List<EventFilter> findFilters(BlockEvent event) {
        List<EventFilter> foundFilters = super.findFilters(event);
        List<String> privateLogBlooms = getPrivateLogBlooms(event);

        filters.stream()
                .filter(f -> !foundFilters.contains(f))
                .filter(
                        filter -> {
                            if (!filter.getNodeId().equals(event.getNodeId())) {
                                return false;
                            }

                            if (filter instanceof ContractEventFilter contractFilter) {
                                return privateLogBlooms.stream()
                                        .anyMatch(
                                                logBloom ->
                                                        BloomFilterUtil.bloomFilterMatch(
                                                                logBloom,
                                                                contractFilter.getContractAddress(),
                                                                filter.getSpecification()
                                                                        .getEventSignature()));
                            }

                            if (filter instanceof GlobalEventFilter) {
                                return privateLogBlooms.stream()
                                        .anyMatch(
                                                logBloom ->
                                                        BloomFilterUtil.bloomFilterMatch(
                                                                logBloom,
                                                                filter.getSpecification()
                                                                        .getEventSignature()));
                            }

                            return false;
                        })
                .forEach(foundFilters::add);

        return foundFilters;
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
