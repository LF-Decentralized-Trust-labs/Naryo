package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "broadcasters")
@TypeAlias("transaction_broadcaster_targets")
public class TransactionBroadcasterTargetDocument extends BroadcasterTargetDocument implements TransactionBroadcasterTargetDescriptor {

    public TransactionBroadcasterTargetDocument(String destination) {
        super(BroadcasterTargetType.TRANSACTION, destination);
    }

    public TransactionBroadcasterTargetDocument() {
        super(BroadcasterTargetType.TRANSACTION, null);
    }
}
