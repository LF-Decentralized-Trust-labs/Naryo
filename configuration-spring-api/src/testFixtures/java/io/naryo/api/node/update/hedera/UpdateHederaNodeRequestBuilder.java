package io.naryo.api.node.update.hedera;

import io.naryo.api.node.update.UpdateNodeRequestBuilder;
import io.naryo.api.node.update.model.UpdateHederaNodeRequest;

public final class UpdateHederaNodeRequestBuilder
        extends UpdateNodeRequestBuilder<UpdateHederaNodeRequestBuilder, UpdateHederaNodeRequest> {
    @Override
    public UpdateHederaNodeRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateHederaNodeRequest build() {
        return new UpdateHederaNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getPrevItemHash());
    }
}
