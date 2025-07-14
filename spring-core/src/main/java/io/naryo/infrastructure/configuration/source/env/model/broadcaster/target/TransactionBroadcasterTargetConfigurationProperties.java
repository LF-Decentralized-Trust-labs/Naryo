package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

public class TransactionBroadcasterTargetConfigurationProperties extends BroadcasterTargetProperties
        implements TransactionBroadcasterTargetDescriptor {

    protected TransactionBroadcasterTargetConfigurationProperties(String destination) {
        super(BroadcasterTargetType.FILTER, destination);
    }

    public TransactionBroadcasterTargetConfigurationProperties() {
        super(BroadcasterTargetType.FILTER, null);
    }
}
