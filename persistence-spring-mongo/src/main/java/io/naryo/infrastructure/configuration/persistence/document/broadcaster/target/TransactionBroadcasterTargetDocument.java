package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("transaction_broadcaster_targets")
public class TransactionBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements TransactionBroadcasterTargetDescriptor {

    public TransactionBroadcasterTargetDocument(List<String> destinations) {
        super(destinations);
    }
}
