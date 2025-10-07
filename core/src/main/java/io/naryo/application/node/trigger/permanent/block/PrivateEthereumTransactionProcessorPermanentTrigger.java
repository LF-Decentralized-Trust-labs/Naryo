package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.util.Objects;

import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PrivateEthereumTransactionProcessorPermanentTrigger
        extends TransactionProcessorPermanentTrigger<PrivateEthereumNode> {

    private final PrivateBlockInteractor interactor;

    public PrivateEthereumTransactionProcessorPermanentTrigger(
            PrivateEthereumNode node,
            LiveRegistry<Filter> filters,
            TransactionEventDispatcherHelper helper,
            PrivateBlockInteractor interactor) {
        super(node, filters, helper);
        Objects.requireNonNull(interactor, "interactor cannot be null");
        this.interactor = interactor;
    }

    @Override
    protected TransactionEvent extractEventFromTransaction(
            Transaction transaction, BlockEvent block) {
        if (isPrivateTransaction(transaction)) {
            String revertReason = transaction.revertReason();
            String to = transaction.to();

            try {
                TransactionReceipt receipt =
                        interactor.getPrivateTransactionReceipt(transaction.hash());
                revertReason = receipt.revertReason();
                to = receipt.to();
            } catch (IOException e) {
                log.error("Failed to get private transaction receipt", e);
            }

            return new TransactionEvent(
                    node.getId(),
                    transaction.hash(),
                    transaction.status() == null || transaction.status().equals("0x1")
                            ? TransactionStatus.CONFIRMED
                            : TransactionStatus.FAILED,
                    new NonNegativeBlockNumber(transaction.nonce()),
                    transaction.blockHash(),
                    new NonNegativeBlockNumber(transaction.blockNumber()),
                    block.getTimestamp(),
                    transaction.index(),
                    transaction.from(),
                    to,
                    transaction.value(),
                    transaction.input(),
                    revertReason);
        }

        return super.extractEventFromTransaction(transaction, block);
    }

    private boolean isPrivateTransaction(Transaction transaction) {
        return transaction.to() != null
                && transaction.to().equals(node.getPrecompiledAddress().value());
    }
}
