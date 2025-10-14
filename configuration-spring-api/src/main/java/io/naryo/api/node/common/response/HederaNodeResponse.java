package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;

public final class HederaNodeResponse extends NodeResponse {

    public HederaNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection) {
        super(id, type, name, subscriptionConfiguration, interactionConfiguration, connection);
    }
}
