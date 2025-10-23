package io.naryo.api.node.common.request.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Pub sub block subscription" )
@Getter
public final class PubSubBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    @Override
    public BlockSubscriptionMethodConfiguration toDomain() {
        return new PubSubBlockSubscriptionMethodConfiguration();
    }
}
