package io.librevents.domain.event.transaction;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ContractTransactionEvent extends TransactionEvent {

    private final String contractAddress;

    public ContractTransactionEvent(
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
            String revertReason,
            String contractAddress) {
        super(
                nodeId,
                hash,
                TransactionType.CONTRACT,
                status,
                nonce,
                blockHash,
                blockNumber,
                blockTimestamp,
                transactionIndex,
                sender,
                receiver,
                value,
                input,
                revertReason);
        this.contractAddress = contractAddress;
        Objects.requireNonNull(contractAddress, "Contract address cannot be null");
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("Contract address cannot be empty");
        }
    }
}
