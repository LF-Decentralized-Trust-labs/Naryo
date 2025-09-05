package io.naryo.infrastructure.store.event.persistence.entity.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.event.transaction.TransactionEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_event")
@Getter
@NoArgsConstructor
public final class TransactionEventEntity {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id") UUID id;

    private @Column(name = "node_id", nullable = false) String nodeId;

    private @Column(name = "transaction_hash", nullable = false) String hash;

    private @Column(name = "nonce", nullable = false) BigInteger nonce;

    private @Column(name = "block_hash", nullable = false) String blockHash;

    private @Column(name = "block_number", nullable = false) BigInteger blockNumber;

    private @Column(name = "block_timestamp", nullable = false) BigInteger blockTimestamp;

    private @Column(name = "transaction_index", nullable = false) BigInteger transactionIndex;

    private @Column(name = "sender", nullable = false) String sender;

    private @Column(name = "receiver", nullable = false) String receiver;

    private @Column(name = "value", nullable = false) String value;

    private @Column(name = "input", nullable = false) String input;

    private @Column(name = "revertReason", nullable = false) String revertReason;

    private @Column(name = "status", nullable = false) TransactionStatus status;

    private TransactionEventEntity(
            String nodeId,
            String hash,
            BigInteger nonce,
            String blockHash,
            BigInteger blockNumber,
            BigInteger blockTimestamp,
            BigInteger transactionIndex,
            String sender,
            String receiver,
            String value,
            String input,
            String revertReason,
            TransactionStatus status) {
        this.nodeId = nodeId;
        this.hash = hash;
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
        this.status = status;
    }

    public static TransactionEventEntity from(TransactionEvent event) {
        return new TransactionEventEntity(
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
}
