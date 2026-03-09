package io.naryo.infrastructure.store.event.persistence.document.transaction;

import java.math.BigInteger;

import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.event.transaction.eth.EthTransactionEvent;
import io.naryo.domain.event.transaction.hedera.HederaTransactionEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(collection = "transaction_event")
public abstract class TransactionEventDocument {

    private final String nodeId;
    private final @MongoId String hash;
    private final BigInteger blockNumber;
    private final BigInteger blockTimestamp;
    private final String sender;
    private final String receiver;
    private final String value;
    private final String status;

    public static TransactionEventDocument fromTransactionEvent(TransactionEvent event) {
        if (event instanceof EthTransactionEvent) {
            return EthTransactionEventDocument.fromEthTransactionEvent((EthTransactionEvent) event);
        } else if (event instanceof HederaTransactionEvent) {
            return HederaTransactionEventDocument.fromHederaTransactionEvent(
                    (HederaTransactionEvent) event);
        }
        throw new IllegalArgumentException("unsupported transaction event type");
    }

    public abstract TransactionEvent toTransactionEvent();
}
