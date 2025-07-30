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

import java.util.List;

import io.naryo.chain.block.AbstractConfirmationBlockListener;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.settings.Node;
import io.naryo.dto.transaction.TransactionDetails;
import io.naryo.dto.transaction.TransactionStatus;
import io.naryo.integration.broadcast.blockchain.BlockchainEventBroadcaster;

public class TransactionConfirmationBlockListener
        extends AbstractConfirmationBlockListener<TransactionDetails> {

    private BlockchainEventBroadcaster eventBroadcaster;
    private OnConfirmedCallback onConfirmedCallback;
    private List<TransactionStatus> statusesToFilter;

    public TransactionConfirmationBlockListener(
            TransactionDetails transactionDetails,
            BlockchainService blockchainService,
            BlockSubscriptionStrategy blockSubscription,
            BlockchainEventBroadcaster eventBroadcaster,
            Node node,
            List<TransactionStatus> statusesToFilter,
            OnConfirmedCallback onConfirmedCallback) {
        super(transactionDetails, blockchainService, blockSubscription, node);
        this.eventBroadcaster = eventBroadcaster;
        this.onConfirmedCallback = onConfirmedCallback;
        this.statusesToFilter = statusesToFilter;
    }

    @Override
    protected void broadcastEventConfirmed() {
        super.broadcastEventConfirmed();

        onConfirmedCallback.onConfirmed();
    }

    @Override
    protected String getEventIdentifier(TransactionDetails transactionDetails) {
        return transactionDetails.getHash() + transactionDetails.getBlockHash();
    }

    @Override
    protected void setStatus(TransactionDetails transactionDetails, String status) {
        transactionDetails.setStatus(TransactionStatus.valueOf(status));
    }

    @Override
    protected void broadcast(TransactionDetails transactionDetails) {
        if (statusesToFilter.contains(transactionDetails.getStatus())) {
            eventBroadcaster.broadcastTransaction(transactionDetails);
        }
    }

    public interface OnConfirmedCallback {
        void onConfirmed();
    }
}
