package io.naryo.api.node.getAll.model.subscription.block;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Pub sub block subscription method")
@Getter
public final class PubSubBlockSubscriptionConfigurationMethodResponse
        extends BlockSubscriptionMethodConfigurationResponse {

    public static PubSubBlockSubscriptionConfigurationMethodResponse fromDomain() {
        return new PubSubBlockSubscriptionConfigurationMethodResponse();
    }
}
