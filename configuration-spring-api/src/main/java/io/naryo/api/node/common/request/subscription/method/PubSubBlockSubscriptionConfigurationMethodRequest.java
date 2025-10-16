package io.naryo.api.node.common.request.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

public final class PubSubBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    PubSubBlockSubscriptionConfigurationMethodRequest() {
        super(BlockSubscriptionMethod.PUBSUB);
    }

    @Override
    public BlockSubscriptionMethodConfiguration toDomain() {
        return new PubSubBlockSubscriptionMethodConfiguration();
    }
}
