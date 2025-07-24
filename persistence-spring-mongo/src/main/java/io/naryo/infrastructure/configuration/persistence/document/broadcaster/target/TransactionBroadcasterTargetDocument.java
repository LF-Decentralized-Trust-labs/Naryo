package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("transaction_broadcaster_targets")
public class TransactionBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements TransactionBroadcasterTargetDescriptor {

    public TransactionBroadcasterTargetDocument(String destination) {
        super(destination);
    }
}
