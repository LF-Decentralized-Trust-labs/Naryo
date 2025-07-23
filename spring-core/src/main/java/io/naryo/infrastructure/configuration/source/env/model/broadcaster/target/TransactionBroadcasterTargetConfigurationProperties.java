package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionBroadcasterTargetConfigurationProperties extends BroadcasterTargetProperties
        implements TransactionBroadcasterTargetDescriptor {

    protected TransactionBroadcasterTargetConfigurationProperties(String destination) {
        super(destination);
    }
}
