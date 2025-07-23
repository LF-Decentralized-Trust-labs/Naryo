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
    default NodeDescriptor merge(NodeDescriptor descriptor) {
        NodeDescriptor.super.merge(descriptor);

        if (descriptor instanceof EthereumNodeDescriptor other) {
            if (!this.getVisibility().equals(other.getVisibility())) {
                return other;
            }
        }
        return this;
    }
}
