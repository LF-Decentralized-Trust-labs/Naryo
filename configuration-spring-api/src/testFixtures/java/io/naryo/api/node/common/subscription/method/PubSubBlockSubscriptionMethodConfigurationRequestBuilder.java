package io.naryo.api.node.common.subscription.method;

import io.naryo.api.node.common.request.subscription.method.PubSubBlockSubscriptionConfigurationMethodRequest;

public final class PubSubBlockSubscriptionMethodConfigurationRequestBuilder
        extends BlockSubscriptionMethodConfigurationRequestBuilder<
                PubSubBlockSubscriptionMethodConfigurationRequestBuilder,
                PubSubBlockSubscriptionConfigurationMethodRequest> {

    @Override
    public PubSubBlockSubscriptionMethodConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public PubSubBlockSubscriptionConfigurationMethodRequest build() {
        return new PubSubBlockSubscriptionConfigurationMethodRequest();
    }
}
