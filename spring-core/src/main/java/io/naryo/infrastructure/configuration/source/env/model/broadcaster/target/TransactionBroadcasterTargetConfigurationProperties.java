package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionBroadcasterTargetConfigurationProperties extends BroadcasterTargetProperties
        implements TransactionBroadcasterTargetDescriptor {

    public TransactionBroadcasterTargetConfigurationProperties(Set<String> destinations) {
        super(destinations);
    }
}
