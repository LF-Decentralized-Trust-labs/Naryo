package io.librevents.domain.event.block;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockEvent extends Event {

    private final NonNegativeBlockNumber number;
    private final String hash;
    private final String logsBloom;
    private final BigInteger size;
    private final BigInteger gasUsed;
    private final BigInteger timestamp;
    private final List<Transaction> transactions;

    public BlockEvent(
            UUID nodeId,
            NonNegativeBlockNumber number,
            String hash,
            String logsBloom,
            BigInteger size,
            BigInteger gasUsed,
            BigInteger timestamp,
            List<Transaction> transactions) {
        super(EventType.BLOCK, nodeId);
        this.number = number;
        this.hash = hash;
        this.logsBloom = logsBloom;
        this.size = size;
        this.gasUsed = gasUsed;
        this.timestamp = timestamp;
        this.transactions = transactions;

        Objects.requireNonNull(number, "number cannot be null");
        Objects.requireNonNull(hash, "hash cannot be null");
        Objects.requireNonNull(logsBloom, "logsBloom cannot be null");
        Objects.requireNonNull(size, "size cannot be null");
        Objects.requireNonNull(gasUsed, "gasUsed cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
        Objects.requireNonNull(transactions, "transactions cannot be null");
        if (hash.isEmpty()) {
            throw new IllegalArgumentException("hash cannot be empty");
        }
        if (logsBloom.isEmpty()) {
            throw new IllegalArgumentException("logsBloom cannot be empty");
        }
        if (size.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        if (gasUsed.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("gasUsed cannot be negative");
        }
        if (timestamp.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("timestamp cannot be negative");
        }
    }
}
