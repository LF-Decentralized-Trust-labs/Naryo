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

package io.naryo.chain.block.tx;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.naryo.chain.block.tx.criteria.TransactionMatchingCriteria;
import io.naryo.chain.factory.TransactionDetailsFactory;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.block.BlockCache;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.domain.Block;
import io.naryo.chain.service.domain.Transaction;
import io.naryo.chain.service.domain.TransactionReceipt;
import io.naryo.chain.service.domain.io.ContractResultResponse;
import io.naryo.chain.service.domain.io.StateChangeResponse;
import io.naryo.chain.service.domain.wrapper.HederaBlock;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.settings.Node;
import io.naryo.chain.settings.NodeSettings;
import io.naryo.dto.transaction.TransactionDetails;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

@Component
@Slf4j
public class DefaultTransactionMonitoringBlockListener
        implements TransactionMonitoringBlockListener {

    // Keyed by node name
    private Map<String, List<TransactionMatchingCriteria>> criteria;

    private ChainServicesContainer chainServicesContainer;

    private BlockchainEventBroadcaster broadcaster;

    private TransactionDetailsFactory transactionDetailsFactory;

    private BlockCache blockCache;

    private Lock lock = new ReentrantLock();

    private NodeSettings nodeSettings;

    public DefaultTransactionMonitoringBlockListener(
            ChainServicesContainer chainServicesContainer,
            BlockchainEventBroadcaster broadcaster,
            TransactionDetailsFactory transactionDetailsFactory,
            BlockCache blockCache,
            NodeSettings nodeSettings) {
        this.criteria = new ConcurrentHashMap<>();

        this.chainServicesContainer = chainServicesContainer;

        this.broadcaster = broadcaster;
        this.transactionDetailsFactory = transactionDetailsFactory;
        this.blockCache = blockCache;
        this.nodeSettings = nodeSettings;
    }

    @Override
    public void onBlock(Block block) {
        lock.lock();

        try {
            processBlock(block);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addMatchingCriteria(TransactionMatchingCriteria matchingCriteria) {

        lock.lock();

        try {
            final String nodeName = matchingCriteria.getNodeName();

            if (!criteria.containsKey(nodeName)) {
                criteria.put(nodeName, new CopyOnWriteArrayList<>());
            }

            criteria.get(nodeName).add(matchingCriteria);

            // Check if any cached blocks match
            // Note, this makes sense for tx hash but maybe doesn't for some other matchers?
            blockCache
                    .getCachedBlocks()
                    .forEach(
                            block ->
                                    block.getTransactions()
                                            .forEach(
                                                    tx ->
                                                            broadcastIfMatched(
                                                                    tx,
                                                                    block,
                                                                    Collections.singletonList(
                                                                            matchingCriteria))));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeMatchingCriteria(TransactionMatchingCriteria matchingCriteria) {
        criteria.get(matchingCriteria.getNodeName()).remove(matchingCriteria);
    }

    private void processBlock(Block block) {
        block.getTransactions().forEach(tx -> broadcastIfMatched(tx, block));
    }

    protected void broadcastIfMatched(
            Transaction tx, Block block, List<TransactionMatchingCriteria> criteriaToCheck) {

        final TransactionDetails txDetails =
                transactionDetailsFactory.createTransactionDetails(
                        tx, TransactionStatus.CONFIRMED, block); // CONFIRMED by default

        if (block instanceof HederaBlock hederaBlock && tx.getTo() == null) {
            List<String> addresses =
                    hederaBlock.getContractResults().stream()
                            .map(ContractResultResponse::getStateChanges)
                            .flatMap(Collection::stream)
                            .toList()
                            .stream()
                            .map(StateChangeResponse::getAddress)
                            .distinct()
                            .toList();
            addresses.forEach(
                    address -> {
                        txDetails.setTo(address);
                        // Only broadcast once, even if multiple matching criteria apply
                        checkTxCriteria(criteriaToCheck, txDetails);
                    });
        } else {
            // Only broadcast once, even if multiple matching criteria apply
            checkTxCriteria(criteriaToCheck, txDetails);
        }
    }

    private void checkTxCriteria(
            List<TransactionMatchingCriteria> criteriaToCheck, TransactionDetails txDetails) {
        criteriaToCheck.stream()
                .filter(matcher -> matcher.isAMatch(txDetails))
                .findFirst()
                .ifPresent(matcher -> onTransactionMatched(txDetails, matcher));
    }

    private void broadcastIfMatched(Transaction tx, Block block) {
        if (criteria.containsKey(block.getNodeName())) {
            broadcastIfMatched(tx, block, criteria.get(block.getNodeName()));
        }
    }

    protected void onTransactionMatched(
            TransactionDetails txDetails, TransactionMatchingCriteria matchingCriteria) {

        final Node node = nodeSettings.getNode(txDetails.getNodeName());
        final BlockchainService blockchainService = getBlockchainService(txDetails.getNodeName());
        final BlockSubscriptionStrategy blockSubscription =
                getBlockSubscriptionStrategy(txDetails.getNodeName());

        final boolean isSuccess = isSuccessTransaction(txDetails);

        if (isSuccess && shouldWaitBeforeConfirmation(node)) {
            txDetails.setStatus(TransactionStatus.UNCONFIRMED);

            blockSubscription.addBlockListener(
                    new TransactionConfirmationBlockListener(
                            txDetails,
                            blockchainService,
                            blockSubscription,
                            broadcaster,
                            node,
                            matchingCriteria.getStatuses(),
                            () -> onConfirmed(txDetails, matchingCriteria)));

            broadcastTransaction(txDetails, matchingCriteria);

            // Don't remove criteria if we're waiting for x blocks, as if there is a fork
            // we need to rebroadcast the unconfirmed tx in new block
        } else {
            if (!isSuccess) {
                txDetails.setStatus(TransactionStatus.FAILED);

                String reason = getRevertReason(txDetails);

                if (reason != null) {
                    txDetails.setRevertReason(reason);
                }
            }

            broadcastTransaction(txDetails, matchingCriteria);

            if (matchingCriteria.isOneTimeMatch()) {
                removeMatchingCriteria(matchingCriteria);
            }
        }
    }

    private void broadcastTransaction(
            TransactionDetails txDetails, TransactionMatchingCriteria matchingCriteria) {
        if (matchingCriteria.getStatuses().contains(txDetails.getStatus())) {
            broadcaster.broadcastTransaction(txDetails);
        }
    }

    private boolean isSuccessTransaction(TransactionDetails txDetails) {
        final TransactionReceipt receipt =
                getBlockchainService(txDetails.getNodeName())
                        .getTransactionReceipt(txDetails.getHash());

        if (receipt.getStatus() == null) {
            // status is only present on Byzantium transactions onwards
            return true;
        }

        return !receipt.getStatus().equals("0x0");
    }

    private boolean shouldWaitBeforeConfirmation(Node node) {
        return !node.getBlocksToWaitForConfirmation().equals(BigInteger.ZERO);
    }

    private BlockchainService getBlockchainService(String nodeName) {
        return chainServicesContainer.getNodeServices(nodeName).getBlockchainService();
    }

    private BlockSubscriptionStrategy getBlockSubscriptionStrategy(String nodeName) {
        return chainServicesContainer.getNodeServices(nodeName).getBlockSubscriptionStrategy();
    }

    private void onConfirmed(
            TransactionDetails txDetails, TransactionMatchingCriteria matchingCriteria) {
        if (matchingCriteria.isOneTimeMatch()) {
            log.debug("Tx {} confirmed, removing matchingCriteria", txDetails.getHash());

            removeMatchingCriteria(matchingCriteria);
        }
    }

    private String getRevertReason(TransactionDetails txDetails) {
        Node node = nodeSettings.getNode(txDetails.getNodeName());

        if (Boolean.TRUE.equals(!node.getAddTransactionRevertReason())
                || txDetails.getRevertReason() != null) {
            return null;
        }

        return getBlockchainService(txDetails.getNodeName())
                .getRevertReason(
                        txDetails.getFrom(),
                        txDetails.getTo(),
                        Numeric.toBigInt(txDetails.getBlockNumber()),
                        txDetails.getInput());
    }
}
