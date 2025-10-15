package io.naryo.api.node.update.model;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

public final class UpdatePublicEthereumNodeRequest extends UpdateEthereumNodeRequest {

    public UpdatePublicEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String prevItemHash) {
        super(
                name,
                subscription,
                interaction,
                connection,
                EthereumNodeVisibility.PUBLIC,
                prevItemHash);
    }
}
