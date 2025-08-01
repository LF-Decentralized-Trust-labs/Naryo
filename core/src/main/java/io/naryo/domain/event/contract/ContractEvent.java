package io.naryo.domain.event.contract;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ContractEvent extends Event {

    private final EventName name;
    private final Set<ContractEventParameter<?>> parameters;
    private final String transactionHash;
    private final BigInteger logIndex;
    private final BigInteger blockNumber;
    private final String blockHash;
    private final String contractAddress;
    private final String sender;
    private final BigInteger timestamp;
    @Setter private ContractEventStatus status;

    public ContractEvent(
            UUID nodeId,
            EventName name,
            Set<ContractEventParameter<?>> parameters,
            String transactionHash,
            BigInteger logIndex,
            BigInteger blockNumber,
            String blockHash,
            String contractAddress,
            String sender,
            ContractEventStatus status,
            BigInteger timestamp) {
        super(EventType.CONTRACT, nodeId);
        this.name = name;
        this.parameters = parameters;
        this.transactionHash = transactionHash;
        this.logIndex = logIndex;
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.contractAddress = contractAddress;
        this.sender = sender;
        this.status = status;
        this.timestamp = timestamp;

        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(parameters, "parameters cannot be null");
        Objects.requireNonNull(transactionHash, "transactionHash cannot be null");
        Objects.requireNonNull(logIndex, "logIndex cannot be null");
        Objects.requireNonNull(blockNumber, "blockNumber cannot be null");
        Objects.requireNonNull(blockHash, "blockHash cannot be null");
        Objects.requireNonNull(contractAddress, "contractAddress cannot be null");
        Objects.requireNonNull(sender, "sender cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
        if (transactionHash.isEmpty()) {
            throw new IllegalArgumentException("transactionHash cannot be empty");
        }
        if (blockHash.isEmpty()) {
            throw new IllegalArgumentException("blockHash cannot be empty");
        }
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("contractAddress cannot be empty");
        }
        if (sender.isEmpty()) {
            throw new IllegalArgumentException("sender cannot be empty");
        }
        if (logIndex.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("logIndex cannot be negative");
        }
        if (blockNumber.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("blockNumber cannot be negative");
        }
        if (timestamp.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("timestamp cannot be negative");
        }
    }
}
