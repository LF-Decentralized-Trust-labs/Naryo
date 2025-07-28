package io.naryo.domain.broadcaster.target;

public class ContractEventBroadcasterTargetBuilder
        extends BroadcasterTargetBuilder<
                ContractEventBroadcasterTargetBuilder, ContractEventBroadcasterTarget> {
    @Override
    public ContractEventBroadcasterTargetBuilder self() {
        return this;
    }

    @Override
    public ContractEventBroadcasterTarget build() {
        return new ContractEventBroadcasterTarget(this.getDestination());
    }
}
