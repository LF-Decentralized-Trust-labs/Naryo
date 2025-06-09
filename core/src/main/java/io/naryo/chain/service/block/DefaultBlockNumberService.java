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

package io.naryo.chain.service.block;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.settings.NodeSettings;
import io.naryo.model.LatestBlock;
import io.naryo.service.EventStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultBlockNumberService implements BlockNumberService {

    private final NodeSettings settings;
    private final EventStoreService eventStoreService;
    private final ChainServicesContainer chainServices;
    private final Map<String, BigInteger> currentBlockAtStartup;

    public DefaultBlockNumberService(
            NodeSettings settings,
            EventStoreService eventStoreService,
            @Lazy ChainServicesContainer chainServices,
            Map<String, BigInteger> currentBlockAtStartup) {
        this.settings = settings;
        this.eventStoreService = eventStoreService;
        this.chainServices = chainServices;
        this.currentBlockAtStartup = currentBlockAtStartup;
    }

    @Override
    public BigInteger getStartBlockForNode(String nodeName) {
        final Optional<LatestBlock> latestBlock = getLatestBlock(nodeName);

        if (latestBlock.isPresent()) {

            // The last block processed
            final BigInteger latestBlockNumber = latestBlock.get().getNumber();
            log.info("Last block number processed on node {}: {}", nodeName, latestBlockNumber);

            final BigInteger maxBlocksToSync = settings.getNode(nodeName).getMaxBlocksToSync();

            BigInteger startBlock =
                    latestBlockNumber.subtract(settings.getNode(nodeName).getNumBlocksToReplay());

            if (maxBlocksToSync.compareTo(BigInteger.ZERO) > 0) {

                // The current block of node
                final BigInteger currentBlock = getCurrentBlockAtStartup(nodeName);
                log.info("Current block for node {}: {}", nodeName, currentBlock);

                // Max blocks to sync enabled, check the difference between current and last synced
                // block
                if (currentBlock.subtract(startBlock).compareTo(maxBlocksToSync) > 0) {
                    log.info("maxBlocksToSync for node {}: {}", nodeName, maxBlocksToSync);
                    // Difference between current block and start block is over max.
                    startBlock = currentBlock.add(BigInteger.ONE).subtract(maxBlocksToSync);
                }
            }

            // Check the replay subtraction result is positive
            startBlock = startBlock.signum() == 1 ? startBlock : BigInteger.ONE;

            log.info("Start block number for node {}: {}", nodeName, startBlock);
            return startBlock;
        }

        final BigInteger initialStartBlock = settings.getNode(nodeName).getInitialStartBlock();

        final BigInteger startBlock =
                initialStartBlock != null ? initialStartBlock : getCurrentBlockAtStartup(nodeName);

        log.info("Start block number for node {}: {}", nodeName, startBlock);

        return startBlock;
    }

    protected Optional<LatestBlock> getLatestBlock(String nodeName) {
        return eventStoreService.getLatestBlock(nodeName);
    }

    // We want to be consistent on the start block across the system, so get the current block once
    // and store
    protected BigInteger getCurrentBlockAtStartup(String node) {
        return currentBlockAtStartup.computeIfAbsent(
                node,
                key ->
                        chainServices
                                .getNodeServices(key)
                                .getBlockchainService()
                                .getCurrentBlockNumber());
    }
}
