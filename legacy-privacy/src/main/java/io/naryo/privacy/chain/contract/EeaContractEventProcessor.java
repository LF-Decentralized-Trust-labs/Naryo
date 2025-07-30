package io.naryo.privacy.chain.contract;

import java.util.List;
import java.util.Objects;

import io.naryo.chain.contract.ContractEventListener;
import io.naryo.chain.contract.DefaultContractEventProcessor;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.container.NodeServices;
import io.naryo.chain.service.domain.Block;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.chain.settings.NodeType;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.privacy.chain.service.Web3JEeaService;
import io.naryo.service.AsyncTaskService;
import io.naryo.utils.ExecutorNameFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class EeaContractEventProcessor extends DefaultContractEventProcessor {

    private final AsyncTaskService asyncTaskService;

    public EeaContractEventProcessor(
            ChainServicesContainer chainServices,
            AsyncTaskService asyncTaskService,
            List<ContractEventListener> contractEventListeners) {
        super(chainServices, asyncTaskService, contractEventListeners);
        this.asyncTaskService = asyncTaskService;
    }

    @Override
    public void processLogsInBlock(Block block, List<ContractEventFilter> contractEventFilters) {
        asyncTaskService
                .executeWithCompletableFuture(
                        ExecutorNameFactory.build(EVENT_EXECUTOR_NAME, block.getNodeName()),
                        () -> {
                            final NodeServices nodeServices =
                                    chainServices.getNodeServices(block.getNodeName());
                            switch (NodeType.valueOf(nodeServices.getNodeType())) {
                                case MIRROR:
                                    this.processLogsInMirrorNodeBlock(block, contractEventFilters);
                                    break;
                                case NORMAL:
                                default:
                                    this.processLogsInNormalNodeBlock(block, contractEventFilters);
                                    break;
                            }
                        })
                .join();
    }

    private void processLogsInNormalNodeBlock(
            Block block, List<ContractEventFilter> contractEventFilters) {
        final BlockchainService blockchainService = getBlockchainService(block.getNodeName());
        final Web3JEeaService web3JEeaService = (Web3JEeaService) blockchainService;

        final List<String> privateTransactionsLogsBloom =
                block.getTransactions().stream()
                        .filter(web3JEeaService::isPrivateTransaction)
                        .map(tx -> web3JEeaService.getPrivateTransactionReceipt(tx.getHash()))
                        .filter(Objects::nonNull)
                        .map(TransactionReceipt::getLogsBloom)
                        .toList();

        contractEventFilters.forEach(
                filter -> {
                    if (!block.getTransactions().isEmpty()
                            && block.getNodeName().equals(filter.getNode())
                            && ((privateTransactionsLogsBloom.stream()
                                            .anyMatch(
                                                    logsBloom ->
                                                            isEventFilterInBloomFilter(
                                                                    filter, logsBloom)))
                                    || isEventFilterInBloomFilter(filter, block.getLogsBloom()))) {
                        processWeb3LogsForFilter(filter, block, blockchainService);
                    }
                });
    }

    private void processWeb3LogsForFilter(
            ContractEventFilter filter, Block block, BlockchainService blockchainService) {

        blockchainService
                .getEventsForFilter(filter, block.getNumber())
                .forEach(
                        event -> {
                            event.setTimestamp(block.getTimestamp());
                            super.triggerListeners(event);
                        });
    }
}
