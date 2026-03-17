package io.naryo.infrastructure.store.event.persistence.document.block;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class TransactionDocument {

    private final String hash;
    private final BigInteger blockNumber;
    private final String from;
    private final String to;
    private final String value;
    private final String fee;
    private final String timestamp;
    private final String status;

    protected TransactionDocument(
            String hash,
            BigInteger blockNumber,
            String from,
            String to,
            String value,
            String fee,
            String timestamp,
            String status) {
        this.hash = hash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.value = value;
        this.fee = fee;
        this.timestamp = timestamp;
        this.status = status;
    }

    public static TransactionDocument fromTransaction(Transaction transaction) {
        if (transaction instanceof EthTransaction ethTransaction) {
            return EthTransactionDocument.fromEthTransaction(ethTransaction);
        } else if (transaction instanceof HederaTransaction hederaTransaction) {
            return HederaTransactionDocument.fromHederaTransaction(hederaTransaction);
        }
        throw new IllegalArgumentException("Unknown transaction type");
    }

    public abstract Transaction toTransaction();
}
