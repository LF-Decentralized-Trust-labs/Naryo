package io.librevents.domain.event.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.common.TransactionStatus;

public final class TransferTransactionEvent extends TransactionEvent {
    public TransferTransactionEvent(
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
        super(
                nodeId,
                hash,
                TransactionType.TRANSFER,
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
    }
}
