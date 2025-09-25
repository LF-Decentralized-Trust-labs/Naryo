package io.naryo.domain.node.eth.pub;

import io.naryo.domain.node.eth.EthereumNodeBuilder;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;

public final class PublicEthereumNodeBuilder
        extends EthereumNodeBuilder<PublicEthereumNodeBuilder, PublicEthereumNode> {

    @Override
    public PublicEthereumNodeBuilder self() {
        return this;
    }

    @Override
    public PublicEthereumNode build() {
        return new PublicEthereumNode(
                getId(),
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
