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

import java.math.BigInteger;

import io.naryo.chain.block.BlockListener;
import io.naryo.chain.block.EventConfirmationBlockListener;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.settings.ChainType;
import io.naryo.chain.settings.Node;
import io.naryo.chain.settings.NodeSettings;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.naryo.integration.eventstore.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * A contract event listener that initialises a block listener after being passed an unconfirmed
 * event.
 *
 * <p>This created block listener counts blocks since the event was first fired and broadcasts a
 * CONFIRMED event once the configured number of blocks have passed.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Component
@Slf4j
public class BroadcastAndInitialiseConfirmationListener extends BaseContractEventListener {

    private final ChainServicesContainer chainServicesContainer;
    private final BlockchainEventBroadcaster eventBroadcaster;
    private final NodeSettings nodeSettings;

    public BroadcastAndInitialiseConfirmationListener(
            ChainServicesContainer chainServicesContainer,
            BlockchainEventBroadcaster eventBroadcaster,
            NodeSettings nodeSettings,
            EventStore eventStore) {
        super(eventStore);
        this.chainServicesContainer = chainServicesContainer;
        this.eventBroadcaster = eventBroadcaster;
        this.nodeSettings = nodeSettings;
    }

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        final Node node = nodeSettings.getNode(eventDetails.getNodeName());
        final ChainType chainType = node.getChainType();

        if (isExistingEvent(eventDetails)) {
            if (chainType == ChainType.ETHEREUM
                    && eventDetails.getStatus() == ContractEventStatus.UNCONFIRMED
                    && !shouldInstantlyConfirm(eventDetails)) {
                log.info(
                        "Registering an EventConfirmationBlockListener for event: {}",
                        eventDetails.getEventIdentifier());
                getBlockSubscriptionStrategy(eventDetails)
                        .addBlockListener(createEventConfirmationBlockListener(eventDetails, node));
            } else {
                eventDetails.setStatus(ContractEventStatus.CONFIRMED);
            }
            eventBroadcaster.broadcastContractEvent(eventDetails);
        }
    }

    protected BlockListener createEventConfirmationBlockListener(
            ContractEventDetails eventDetails, Node node) {
        return new EventConfirmationBlockListener(
                eventDetails,
                getBlockchainService(eventDetails),
                getBlockSubscriptionStrategy(eventDetails),
                eventBroadcaster,
                node);
    }

    private BlockchainService getBlockchainService(ContractEventDetails eventDetails) {
        return chainServicesContainer
                .getNodeServices(eventDetails.getNodeName())
                .getBlockchainService();
    }

    private BlockSubscriptionStrategy getBlockSubscriptionStrategy(
            ContractEventDetails eventDetails) {
        return chainServicesContainer
                .getNodeServices(eventDetails.getNodeName())
                .getBlockSubscriptionStrategy();
    }

    private boolean shouldInstantlyConfirm(ContractEventDetails eventDetails) {
        final BlockchainService blockchainService = getBlockchainService(eventDetails);
        final Node node = nodeSettings.getNode(blockchainService.getNodeName());
        BigInteger currentBlock = blockchainService.getCurrentBlockNumber();
        BigInteger waitBlocks = node.getBlocksToWaitForConfirmation();

        return currentBlock.compareTo(eventDetails.getBlockNumber().add(waitBlocks)) >= 0
                && isTransactionStillInBlock(
                        eventDetails.getTransactionHash(),
                        eventDetails.getBlockHash(),
                        blockchainService);
    }

    private boolean isTransactionStillInBlock(
            String txHash, String blockHash, BlockchainService blockchainService) {
        final TransactionReceipt receipt = blockchainService.getTransactionReceipt(txHash);

        return receipt != null && receipt.getBlockHash().equals(blockHash);
    }
}
