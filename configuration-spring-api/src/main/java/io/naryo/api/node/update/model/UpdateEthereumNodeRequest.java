package io.naryo.api.node.update.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "visibility")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UpdatePublicEthereumNodeRequest.class, name = "PUBLIC"),
    @JsonSubTypes.Type(value = UpdatePrivateEthereumNodeRequest.class, name = "PRIVATE"),
})
public abstract class UpdateEthereumNodeRequest extends UpdateNodeRequest {

    protected final @NotNull EthereumNodeVisibility visibility;

    public UpdateEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            EthereumNodeVisibility visibility,
            String prevItemHash) {
        super(name, NodeType.ETHEREUM, subscription, interaction, connection, prevItemHash);
        this.visibility = visibility;
    }
}
