package io.naryo.domain.broadcaster.target;

public class TransactionBroadcasterTargetBuilder
        extends BroadcasterTargetBuilder<
                TransactionBroadcasterTargetBuilder, TransactionBroadcasterTarget> {
    @Override
    public TransactionBroadcasterTargetBuilder self() {
        return this;
    }

    @Override
    public TransactionBroadcasterTarget build() {
        return new TransactionBroadcasterTarget(this.getDestinations());
    }
}
