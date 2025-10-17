package io.naryo.api.node.common.hedera;

import io.naryo.api.node.common.NodeRequestBuilder;
import io.naryo.api.node.common.request.HederaNodeRequest;

public final class HederaNodeRequestBuilder
        extends NodeRequestBuilder<HederaNodeRequestBuilder, HederaNodeRequest> {
    @Override
    public HederaNodeRequestBuilder self() {
        return this;
    }

    @Override
    public HederaNodeRequest build() {
        return new HederaNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection());
    }
}
