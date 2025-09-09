package io.naryo.domain.broadcaster.target;

public class AllBroadcasterTargetBuilder
        extends BroadcasterTargetBuilder<AllBroadcasterTargetBuilder, AllBroadcasterTarget> {

    @Override
    public AllBroadcasterTargetBuilder self() {
        return this;
    }

    @Override
    public AllBroadcasterTarget build() {
        return new AllBroadcasterTarget(this.getDestinations());
    }
}
