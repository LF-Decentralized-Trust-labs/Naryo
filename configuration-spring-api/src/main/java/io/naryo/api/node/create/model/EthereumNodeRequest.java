package io.naryo.api.node.create.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "visibility")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicEthereumNodeRequest.class, name = "PUBLIC"),
    @JsonSubTypes.Type(value = PrivateEthereumNodeRequest.class, name = "PRIVATE"),
})
@Getter
public abstract class EthereumNodeRequest extends CreateNodeRequest {

    private final @NotNull EthereumNodeVisibility visibility;

    public EthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            EthereumNodeVisibility visibility) {
        super(name, NodeType.ETHEREUM, subscription, interaction, connection);
        this.visibility = visibility;
    }
}
