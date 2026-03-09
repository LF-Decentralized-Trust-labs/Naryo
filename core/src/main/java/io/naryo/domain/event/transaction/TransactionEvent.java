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
public abstract class TransactionEvent extends Event<String> {

    private final String hash;
    private final NonNegativeBlockNumber blockNumber;
    private final BigInteger blockTimestamp;
    private final String sender;
    private final String receiver;
    private final String value;
    @Setter private TransactionStatus status;

    public TransactionEvent(
            UUID nodeId,
            String hash,
            TransactionStatus status,
            NonNegativeBlockNumber blockNumber,
            BigInteger blockTimestamp,
            String sender,
            String receiver,
            String value) {
        super(EventType.TRANSACTION, nodeId);
        this.hash = hash;
        this.status = status;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;

        Objects.requireNonNull(hash, "hash cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(blockNumber, "blockNumber cannot be null");
        Objects.requireNonNull(blockTimestamp, "blockTimestamp cannot be null");

        if (hash.isEmpty()) {
            throw new IllegalArgumentException("hash cannot be empty");
        }
        if (blockTimestamp.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("blockTimestamp cannot be negative");
        }
    }

    @Override
    public String getKey() {
        return hash;
    }
}
