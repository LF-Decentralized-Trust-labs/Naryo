package io.naryo.application.configuration.source.model.node;

import io.naryo.domain.node.NodeType;

public interface HederaNodeDescriptor extends NodeDescriptor {

    @Override
    default NodeType getType() {
        return NodeType.HEDERA;
    }

    @Override
    default NodeDescriptor merge(NodeDescriptor other) {
        if (!(other instanceof HederaNodeDescriptor otherHederaNode)) {
            return this;
        }

        return NodeDescriptor.super.merge(otherHederaNode);
    }
}
