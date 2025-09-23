package io.naryo.domain.node.hedera;

import io.naryo.domain.node.NodeBuilder;

public final class HederaNodeBuilder extends NodeBuilder<HederaNodeBuilder, HederaNode> {
    @Override
    public HederaNodeBuilder self() {
        return this;
    }

    @Override
    public HederaNode build() {
        return new HederaNode(
                getId(),
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
