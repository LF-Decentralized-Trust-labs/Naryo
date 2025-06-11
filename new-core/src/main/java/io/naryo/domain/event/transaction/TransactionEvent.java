package io.naryo.domain.event.transaction;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransactionEvent extends Event {

    private final String hash;
    private final NonNegativeBlockNumber nonce;
    private final String blockHash;
    private final NonNegativeBlockNumber blockNumber;
    private final BigInteger blockTimestamp;
    private final BigInteger transactionIndex;
    private final String sender;
    private final String receiver;
    private final String value;
    private final String input;
    @Setter private String revertReason;
    @Setter private TransactionStatus status;

    public TransactionEvent(
            UUID nodeId,
            String hash,
            TransactionStatus status,
            NonNegativeBlockNumber nonce,
            String blockHash,
            NonNegativeBlockNumber blockNumber,
            BigInteger blockTimestamp,
            BigInteger transactionIndex,
            String sender,
            String receiver,
            String value,
            String input,
            String revertReason) {
        super(EventType.TRANSACTION, nodeId);
        this.hash = hash;
        this.status = status;
        this.nonce = nonce;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.transactionIndex = transactionIndex;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.input = input;
        this.revertReason = revertReason;

        Objects.requireNonNull(hash, "hash cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(nonce, "nonce cannot be null");
        Objects.requireNonNull(blockHash, "blockHash cannot be null");
        Objects.requireNonNull(blockNumber, "blockNumber cannot be null");
        Objects.requireNonNull(blockTimestamp, "blockTimestamp cannot be null");
        Objects.requireNonNull(transactionIndex, "transactionIndex cannot be null");
        Objects.requireNonNull(sender, "sender cannot be null");
        Objects.requireNonNull(receiver, "receiver cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(input, "input cannot be null");
        Objects.requireNonNull(revertReason, "revertReason cannot be null");

        if (hash.isEmpty()) {
            throw new IllegalArgumentException("hash cannot be empty");
        }
        if (blockHash.isEmpty()) {
            throw new IllegalArgumentException("blockHash cannot be empty");
        }
        if (sender.isEmpty()) {
            throw new IllegalArgumentException("sender cannot be empty");
        }
        if (receiver.isEmpty()) {
            throw new IllegalArgumentException("receiver cannot be empty");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be empty");
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException("input cannot be empty");
        }
        if (blockTimestamp.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("blockTimestamp cannot be negative");
        }
        if (transactionIndex.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("transactionIndex cannot be negative");
        }
    }
}
