package io.naryo.domain.event.transaction.eth;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class EthTransactionEvent extends TransactionEvent {

    private final String blockHash;
    private final NonNegativeBlockNumber nonce;
    private final BigInteger transactionIndex;
    private final String input;
    @Setter private String revertReason;

    public EthTransactionEvent(
            UUID nodeId,
            String hash,
            TransactionStatus status,
            NonNegativeBlockNumber blockNumber,
            BigInteger blockTimestamp,
            String sender,
            String receiver,
            String value,
            String blockHash,
            NonNegativeBlockNumber nonce,
            BigInteger transactionIndex,
            String input,
            String revertReason) {
        super(nodeId, hash, status, blockNumber, blockTimestamp, sender, receiver, value);
        this.blockHash = blockHash;
        this.nonce = nonce;
        this.transactionIndex = transactionIndex;
        this.input = input;
        this.revertReason = revertReason;

        Objects.requireNonNull(blockHash, "blockHash cannot be null");
        Objects.requireNonNull(nonce, "nonce cannot be null");
        Objects.requireNonNull(transactionIndex, "transactionIndex cannot be null");
        Objects.requireNonNull(input, "input cannot be null");
        Objects.requireNonNull(revertReason, "revertReason cannot be null");

        if (blockHash.isEmpty()) {
            throw new IllegalArgumentException("blockHash cannot be empty");
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException("input cannot be empty");
        }
        if (transactionIndex.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("transactionIndex cannot be negative");
        }
    }
}
