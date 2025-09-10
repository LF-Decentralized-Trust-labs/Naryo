package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;

import io.naryo.application.node.interactor.block.dto.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_event_transaction")
@Getter
@NoArgsConstructor
public final class BlockEventTransactionEntity {

    private @Id @Column(name = "hash", nullable = false) String hash;

    private @Column(name = "index", nullable = false) BigInteger index;

    private @Column(name = "nonce", nullable = false) BigInteger nonce;

    private @Column(name = "block_number", nullable = false) BigInteger blockNumber;

    private @Column(name = "block_hash", nullable = false) String blockHash;

    private @Column(name = "status", nullable = false) String status;

    private @Column(name = "`from`", nullable = false) String from;

    private @Column(name = "`to`", nullable = false) String to;

    private @Column(name = "value") String value;

    private @Column(name = "input", nullable = false) String input;

    private @Column(name = "log_bloom", nullable = false) String logBloom;

    private @Column(name = "revert_reason", nullable = false) String revertReason;

    private BlockEventTransactionEntity(
            String hash,
            BigInteger index,
            BigInteger nonce,
            BigInteger blockNumber,
            String blockHash,
            String status,
            String from,
            String to,
            String value,
            String input,
            String logBloom,
            String revertReason) {
        this.hash = hash;
        this.index = index;
        this.nonce = nonce;
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.status = status;
        this.from = from;
        this.to = to;
        this.value = value;
        this.input = input;
        this.logBloom = logBloom;
        this.revertReason = revertReason;
    }

    public static BlockEventTransactionEntity fromTransaction(Transaction transaction) {
        return new BlockEventTransactionEntity(
                transaction.hash(),
                transaction.index(),
                transaction.nonce(),
                transaction.blockNumber(),
                transaction.blockHash(),
                transaction.status(),
                transaction.from(),
                transaction.to(),
                transaction.value(),
                transaction.input(),
                transaction.logBloom(),
                transaction.revertReason());
    }

    public Transaction toTransaction() {
        return new Transaction(
                hash,
                index,
                nonce,
                blockNumber,
                blockHash,
                status,
                from,
                to,
                value,
                input,
                logBloom,
                revertReason);
    }
}
