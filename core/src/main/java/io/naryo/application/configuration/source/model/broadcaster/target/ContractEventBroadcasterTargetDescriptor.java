package io.naryo.application.configuration.source.model.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTargetType;

public interface ContractEventBroadcasterTargetDescriptor extends BroadcasterTargetDescriptor {

    @Override
    default BroadcasterTargetType getType() {
        return BroadcasterTargetType.CONTRACT_EVENT;
    }
}
