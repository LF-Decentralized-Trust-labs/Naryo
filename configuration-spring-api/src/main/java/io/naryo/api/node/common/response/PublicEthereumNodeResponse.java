package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;

public class PublicEthereumNodeResponse extends NodeResponse {

    private final String visibility;

    public PublicEthereumNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection,
            String visibility) {
        super(id, type, name, subscriptionConfiguration, interactionConfiguration, connection);
        this.visibility = visibility;
    }
}
