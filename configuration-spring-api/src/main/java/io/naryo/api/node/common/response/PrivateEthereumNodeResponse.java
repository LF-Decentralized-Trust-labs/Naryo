package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;
import lombok.Builder;

@Builder
public record PrivateEthereumNodeResponse(
        UUID id,
        String type,
        String name,
        SubscriptionConfigurationResponse subscriptionConfiguration,
        InteractionConfigurationResponse interactionConfiguration,
        NodeConnectionResponse connection,
        String visibility,
        String groupId,
        String precompiledAddress)
        implements NodeResponse {}
