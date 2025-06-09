/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.chain.contract;

import java.util.List;

import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.HederaService;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.container.NodeServices;
import io.naryo.chain.service.domain.Block;
import io.naryo.chain.service.domain.io.ContractResultResponse;
import io.naryo.chain.service.domain.io.HederaLogResponse;
import io.naryo.chain.service.domain.wrapper.HederaBlock;
import io.naryo.chain.settings.NodeType;
import io.naryo.chain.util.BloomFilterUtil;
import io.naryo.chain.util.Web3jUtil;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.service.AsyncTaskService;
import io.naryo.utils.ExecutorNameFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultContractEventProcessor implements ContractEventProcessor {

    public static final String EVENT_EXECUTOR_NAME = "EVENT";

    protected ChainServicesContainer chainServices;

    private AsyncTaskService asyncTaskService;

    private List<ContractEventListener> contractEventListeners;

    @Override
    public void processLogsInBlock(Block block, List<ContractEventFilter> contractEventFilters) {
        asyncTaskService
                .executeWithCompletableFuture(
                        ExecutorNameFactory.build(EVENT_EXECUTOR_NAME, block.getNodeName()),
                        () -> {
                            final NodeServices nodeServices =
                                    this.chainServices.getNodeServices(block.getNodeName());
                            final NodeType nodeType = NodeType.valueOf(nodeServices.getNodeType());
                            if (nodeType == NodeType.MIRROR) {
                                this.processLogsInMirrorNodeBlock(block, contractEventFilters);
                                return;
                            }
                            contractEventFilters.forEach(
                                    filter ->
                                            processLogsForFilter(
                                                    filter,
                                                    block,
                                                    nodeServices.getBlockchainService()));
                        })
                .join();
    }

    @Override
    public void processContractEvent(ContractEventDetails contractEventDetails) {
        asyncTaskService
                .executeWithCompletableFuture(
                        ExecutorNameFactory.build(
                                EVENT_EXECUTOR_NAME, contractEventDetails.getNodeName()),
                        () -> triggerListeners(contractEventDetails))
                .join();
    }

    protected boolean isEventFilterInBloomFilter(ContractEventFilter filter, String logsBloom) {
        final BloomFilterUtil.BloomFilterBits bloomBits = BloomFilterUtil.getBloomBits(filter);

        return BloomFilterUtil.bloomFilterMatch(logsBloom, bloomBits);
    }

    protected BlockchainService getBlockchainService(String nodeName) {
        return chainServices.getNodeServices(nodeName).getBlockchainService();
    }

    protected HederaService getHederaService(String nodeName) {
        return chainServices.getNodeServices(nodeName).getHederaService();
    }

    protected void processLogsInMirrorNodeBlock(
            Block block, List<ContractEventFilter> contractEventFilters) {
        final HederaService hederaService = getHederaService(block.getNodeName());
        List<ContractResultResponse> contractResultResponseList =
                ((HederaBlock) block).getContractResults();
        if (contractResultResponseList != null && !contractResultResponseList.isEmpty()) {
            contractResultResponseList.forEach(
                    res -> processTransactionData(hederaService, res, contractEventFilters));
        }
    }

    protected void triggerListeners(ContractEventDetails contractEvent) {
        contractEventListeners.forEach(listener -> triggerListener(listener, contractEvent));
    }

    private void processLogsForFilter(
            ContractEventFilter filter, Block block, BlockchainService blockchainService) {

        if (block.getNodeName().equals(filter.getNode())
                && isEventFilterInBloomFilter(filter, block.getLogsBloom())) {
            blockchainService
                    .getEventsForFilter(filter, block.getNumber())
                    .forEach(
                            event -> {
                                event.setTimestamp(block.getTimestamp());
                                event.setBlockTimestamp(block.getTimestamp());
                                triggerListeners(event);
                            });
        }
    }

    private void triggerListener(
            ContractEventListener listener, ContractEventDetails contractEventDetails) {
        try {
            listener.onEvent(contractEventDetails);
        } catch (RuntimeException t) {
            log.error(
                    "An error occurred when processing contractEvent with id {}",
                    contractEventDetails.getEventIdentifier(),
                    t);
            throw t;
        }
    }

    private void processMirrorLogsForFilter(
            ContractEventFilter filter,
            HederaService hederaService,
            HederaLogResponse hederaLogResponse,
            ContractResultResponse contractResult) {
        triggerListeners(
                hederaService.getEventForFilter(filter, hederaLogResponse, contractResult));
    }

    private void processTransactionData(
            HederaService hederaService,
            ContractResultResponse contractResult,
            List<ContractEventFilter> contractEventFilters) {
        contractResult
                .getLogs()
                .forEach(
                        hederaLogResponse ->
                                contractEventFilters.forEach(
                                        filter -> {
                                            if (isEventFilterInTopic(hederaLogResponse, filter)
                                                    && isContractFromEventFilter(
                                                            hederaLogResponse, filter)) {
                                                processMirrorLogsForFilter(
                                                        filter,
                                                        hederaService,
                                                        hederaLogResponse,
                                                        contractResult);
                                            }
                                        }));
    }

    private boolean isEventFilterInTopic(HederaLogResponse log, ContractEventFilter filter) {
        String eventSignature = Web3jUtil.getSignature(filter.getEventSpecification());
        return log.getTopics().getFirst().equals(eventSignature);
    }

    private boolean isContractFromEventFilter(HederaLogResponse log, ContractEventFilter filter) {
        return filter.getContractAddress().equalsIgnoreCase(log.getAddress());
    }
}
