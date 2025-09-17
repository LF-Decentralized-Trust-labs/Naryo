package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ContractEventBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements ContractEventBroadcasterTargetDescriptor {

    public ContractEventBroadcasterTargetProperties(Set<String> destinations) {
        super(destinations);
    }
}
