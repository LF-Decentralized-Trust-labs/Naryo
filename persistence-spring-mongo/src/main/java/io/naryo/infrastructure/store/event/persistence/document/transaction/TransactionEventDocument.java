package io.naryo.infrastructure.store.event.persistence.document.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "transaction_event")
@TypeAlias("transaction_event")
@AllArgsConstructor
public final class TransactionEventDocument {

    private final String nodeId;
    private final @MongoId String hash;
    private final BigInteger nonce;
    private final String blockHash;
    private final BigInteger blockNumber;
    private final BigInteger blockTimestamp;
    private final BigInteger transactionIndex;
    private final String sender;
    private final String receiver;
    private final String value;
    private final String input;
    private String revertReason;
    private TransactionStatus status;

    public static TransactionEventDocument fromTransactionEvent(TransactionEvent event) {
        return new TransactionEventDocument(
                event.getNodeId().toString(),
                event.getHash(),
                event.getNonce().value(),
                event.getBlockHash(),
                event.getBlockNumber().value(),
                event.getBlockTimestamp(),
                event.getTransactionIndex(),
                event.getSender(),
                event.getReceiver(),
                event.getValue(),
                event.getInput(),
                event.getRevertReason(),
                event.getStatus());
    }

    public TransactionEvent toTransactionEvent() {
        return new TransactionEvent(
                UUID.fromString(nodeId),
                hash,
                status,
                new NonNegativeBlockNumber(nonce),
                blockHash,
                new NonNegativeBlockNumber(blockNumber),
                blockTimestamp,
                transactionIndex,
                sender,
                receiver,
                value,
                input,
                revertReason);
    }
}
