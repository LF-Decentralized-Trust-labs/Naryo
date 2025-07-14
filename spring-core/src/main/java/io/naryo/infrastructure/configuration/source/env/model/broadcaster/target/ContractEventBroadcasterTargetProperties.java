package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

public final class ContractEventBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements ContractEventBroadcasterTargetDescriptor {

    public ContractEventBroadcasterTargetProperties(String destination) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destination);
    }

    public ContractEventBroadcasterTargetProperties() {
        super(BroadcasterTargetType.CONTRACT_EVENT, null);
    }
}
