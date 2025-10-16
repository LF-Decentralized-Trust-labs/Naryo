package io.naryo.api.node.create.hedera;

import io.naryo.api.node.create.CreateNodeRequestBuilder;
import io.naryo.api.node.create.model.CreateHederaNodeRequest;

public final class CreateHederaNodeRequestBuilder
        extends CreateNodeRequestBuilder<CreateHederaNodeRequestBuilder, CreateHederaNodeRequest> {
    @Override
    public CreateHederaNodeRequestBuilder self() {
        return this;
    }

    @Override
    public CreateHederaNodeRequest build() {
        return new CreateHederaNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
