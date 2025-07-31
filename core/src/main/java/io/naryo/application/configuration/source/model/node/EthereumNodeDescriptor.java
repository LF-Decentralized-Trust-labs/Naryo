package io.naryo.application.configuration.source.model.node;

import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

public interface EthereumNodeDescriptor extends NodeDescriptor {

    EthereumNodeVisibility getVisibility();

    @Override
    default NodeType getType() {
        return NodeType.ETHEREUM;
    }

    @Override
    default NodeDescriptor merge(NodeDescriptor other) {
        if (!(other instanceof EthereumNodeDescriptor otherEthereumNode)) {
            return this;
        }

        return NodeDescriptor.super.merge(otherEthereumNode);
    }
}
