package io.naryo.infrastructure.store.event.persistence.entity.transaction;

import java.math.BigInteger;
import java.util.UUID;

import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class TransactionEventEntity {
    private @Column(name = "node_id", nullable = false) UUID nodeId;
    private @Id @Column(name = "transaction_hash", nullable = false) String hash;
    private @Column(name = "block_number", nullable = false) BigInteger blockNumber;
    private @Column(name = "block_timestamp", nullable = false) BigInteger blockTimestamp;
    private @Column(name = "sender", nullable = false) String sender;
    private @Column(name = "receiver", nullable = false) String receiver;
    private @Column(name = "value", nullable = false) String value;

    @Column(name = "status", nullable = false)
    private String status;

    public static TransactionEventEntity fromTransactionEvent(TransactionEvent event) {
        if (event instanceof EthTransactionEvent ethTransactionEvent) {
            return EthTransactionEventEntity.fromEthTransactionEvent(ethTransactionEvent);
        } else if (event instanceof HederaTransactionEvent hederaTransactionEvent) {
            return HederaTransactionEventEntity.fromHederaTransactionEvent(hederaTransactionEvent);
        }
        throw new IllegalArgumentException("unsupported transaction event type");
    }

    public abstract TransactionEvent toTransactionEvent();
}
