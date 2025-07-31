package io.naryo.domain.broadcaster.target;

public class BlockBroadcasterTargetBuilder
        extends BroadcasterTargetBuilder<BlockBroadcasterTargetBuilder, BlockBroadcasterTarget> {
    @Override
    public BlockBroadcasterTargetBuilder self() {
        return this;
    }

    @Override
    public BlockBroadcasterTarget build() {
        return new BlockBroadcasterTarget(this.getDestination());
    }
}
