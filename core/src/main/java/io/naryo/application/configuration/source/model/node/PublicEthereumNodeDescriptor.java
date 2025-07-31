package io.naryo.application.configuration.source.model.node;

import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

public interface PublicEthereumNodeDescriptor extends EthereumNodeDescriptor {

    @Override
    default EthereumNodeVisibility getVisibility() {
        return EthereumNodeVisibility.PUBLIC;
    }

    @Override
    default NodeDescriptor merge(NodeDescriptor other) {
        if (!(other instanceof PublicEthereumNodeDescriptor otherPublicEthereumNode)) {
            return this;
        }

        return EthereumNodeDescriptor.super.merge(otherPublicEthereumNode);
    }
}
