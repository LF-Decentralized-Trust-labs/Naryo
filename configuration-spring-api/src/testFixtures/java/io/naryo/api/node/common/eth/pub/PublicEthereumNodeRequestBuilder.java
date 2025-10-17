package io.naryo.api.node.common.eth.pub;

import io.naryo.api.node.common.eth.EthereumNodeRequestBuilder;
import io.naryo.api.node.common.request.PublicEthereumNodeRequest;

public final class PublicEthereumNodeRequestBuilder
        extends EthereumNodeRequestBuilder<
                PublicEthereumNodeRequestBuilder, PublicEthereumNodeRequest> {

    @Override
    public PublicEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public PublicEthereumNodeRequest build() {
        return new PublicEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
