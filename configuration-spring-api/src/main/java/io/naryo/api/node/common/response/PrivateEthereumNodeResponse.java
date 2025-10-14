package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;

public final class PrivateEthereumNodeResponse extends NodeResponse {

    private final String visibility;
    private final String groupId;
    private final String precompiledAddress;

    public PrivateEthereumNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection,
            String visibility,
            String groupId,
            String precompiledAddress) {
        super(id, type, name, subscriptionConfiguration, interactionConfiguration, connection);
        this.visibility = visibility;
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }
}
