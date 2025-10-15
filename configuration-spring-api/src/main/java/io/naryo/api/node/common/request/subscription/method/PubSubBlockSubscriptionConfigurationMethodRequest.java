package io.naryo.api.node.common.request.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public final class PubSubBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    PubSubBlockSubscriptionConfigurationMethodRequest() {
        super(BlockSubscriptionMethod.PUBSUB);
    }
}
