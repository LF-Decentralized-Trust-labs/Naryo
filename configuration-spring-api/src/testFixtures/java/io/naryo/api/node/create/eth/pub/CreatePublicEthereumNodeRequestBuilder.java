package io.naryo.api.node.create.eth.pub;

import io.naryo.api.node.create.eth.CreateEthereumNodeRequestBuilder;
import io.naryo.api.node.create.model.CreatePublicEthereumNodeRequest;

public final class CreatePublicEthereumNodeRequestBuilder
        extends CreateEthereumNodeRequestBuilder<
                CreatePublicEthereumNodeRequestBuilder, CreatePublicEthereumNodeRequest> {

    @Override
    public CreatePublicEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public CreatePublicEthereumNodeRequest build() {
        return new CreatePublicEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
