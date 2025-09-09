package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ContractEventBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements ContractEventBroadcasterTargetDescriptor {

    public ContractEventBroadcasterTargetProperties(List<String> destinations) {
        super(destinations);
    }
}
