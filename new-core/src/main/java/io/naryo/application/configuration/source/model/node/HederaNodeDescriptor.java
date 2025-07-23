package io.naryo.application.configuration.source.model.node;

import io.naryo.domain.node.NodeType;

public interface HederaNodeDescriptor extends NodeDescriptor {

    @Override
    default NodeType getType() {
        return NodeType.HEDERA;
    }
}
