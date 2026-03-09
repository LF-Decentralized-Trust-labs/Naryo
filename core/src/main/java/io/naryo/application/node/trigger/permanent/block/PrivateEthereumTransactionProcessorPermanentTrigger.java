package io.naryo.application.node.trigger.permanent.block;

import java.io.IOException;
import java.util.Objects;

import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.TransactionReceipt;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.priv.PrivateBlockInteractor;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
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
        if (isPrivateTransaction(transaction)
                && transaction instanceof EthTransaction ethTransaction) {
            String revertReason = ethTransaction.getRevertReason();
            String to = ethTransaction.getTo();

            try {
                TransactionReceipt receipt =
                        interactor.getPrivateTransactionReceipt(transaction.getHash());
                revertReason = receipt.revertReason();
                to = receipt.to();
            } catch (IOException e) {
                log.error("Failed to get private transaction receipt", e);
            }

            return new EthTransactionEvent(
                    node.getId(),
                    ethTransaction.getHash(),
                    ethTransaction.getStatus() == null || ethTransaction.getStatus().equals("0x1")
                            ? TransactionStatus.CONFIRMED
                            : TransactionStatus.FAILED,
                    new NonNegativeBlockNumber(ethTransaction.getBlockNumber()),
                    block.getTimestamp(),
                    ethTransaction.getFrom(),
                    to,
                    ethTransaction.getValue(),
                    ethTransaction.getBlockHash(),
                    new NonNegativeBlockNumber(ethTransaction.getNonce()),
                    ethTransaction.getIndex(),
                    ethTransaction.getInput(),
                    revertReason);
        }

        return super.extractEventFromTransaction(transaction, block);
    }

    private boolean isPrivateTransaction(Transaction transaction) {
        return transaction.getTo() != null
                && transaction.getTo().equals(node.getPrecompiledAddress().value());
    }
}
