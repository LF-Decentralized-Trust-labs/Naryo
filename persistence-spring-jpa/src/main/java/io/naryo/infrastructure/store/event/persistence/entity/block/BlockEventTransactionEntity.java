package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.application.node.interactor.block.dto.eth.EthTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_event_transaction")
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BlockEventTransactionEntity {

    @Id
    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "block_number", nullable = false)
    private BigInteger blockNumber;

    @Column(name = "from_address", nullable = false)
    private String from;

    @Column(name = "to_address")
    private String to;

    @Column(name = "value")
    private String value;

    @Column(name = "fee")
    private String fee;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "status")
    private String status;

    protected BlockEventTransactionEntity(
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

    public abstract Transaction toTransaction();

    public static BlockEventTransactionEntity fromTransaction(Transaction transaction) {
        if (transaction instanceof EthTransaction) {
            return new EthBlockEventTransactionEntity((EthTransaction) transaction);
        } else if (transaction instanceof HederaTransaction) {
            return new HederaBlockEventTransactionEntity((HederaTransaction) transaction);
        } else {
            throw new IllegalArgumentException(
                    "Unknown transaction type: " + transaction.getClass().getName());
        }
    }
}
