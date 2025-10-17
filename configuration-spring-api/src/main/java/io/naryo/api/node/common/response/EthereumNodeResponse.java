package io.naryo.api.node.common.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "visibility")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicEthereumNodeResponse.class, name = "PUBLIC"),
    @JsonSubTypes.Type(value = PrivateEthereumNodeResponse.class, name = "PRIVATE"),
})
public abstract class EthereumNodeResponse extends NodeResponse {

    protected final String visibility;

    protected EthereumNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection,
            String currentItemHash,
            String visibility) {
        super(
                id,
                type,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                currentItemHash);
        this.visibility = visibility;
    }
}
