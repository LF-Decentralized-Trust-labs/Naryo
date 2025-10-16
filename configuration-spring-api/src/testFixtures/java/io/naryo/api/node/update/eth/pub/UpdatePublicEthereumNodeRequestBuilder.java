package io.naryo.api.node.update.eth.pub;

import io.naryo.api.node.update.eth.UpdateEthereumNodeRequestBuilder;
import io.naryo.api.node.update.model.UpdatePublicEthereumNodeRequest;

public final class UpdatePublicEthereumNodeRequestBuilder
        extends UpdateEthereumNodeRequestBuilder<
                UpdatePublicEthereumNodeRequestBuilder, UpdatePublicEthereumNodeRequest> {

    @Override
    public UpdatePublicEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public UpdatePublicEthereumNodeRequest build() {
        return new UpdatePublicEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getPrevItemHash());
    }
}
