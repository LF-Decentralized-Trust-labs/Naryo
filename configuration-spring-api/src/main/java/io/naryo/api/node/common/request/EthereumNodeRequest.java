package io.naryo.api.node.common.request;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Base Ethereum node request")
@Getter
public abstract class EthereumNodeRequest extends NodeRequest {

    public EthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        super(name, subscription, interaction, connection);
    }
}
