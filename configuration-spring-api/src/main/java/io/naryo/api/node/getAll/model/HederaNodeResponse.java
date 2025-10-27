package io.naryo.api.node.getAll.model;

import java.util.UUID;

import io.naryo.api.node.getAll.model.connection.NodeConnectionResponse;
import io.naryo.api.node.getAll.model.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.getAll.model.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.hedera.HederaNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Hedera node")
@Getter
public final class HederaNodeResponse extends NodeResponse {

    public HederaNodeResponse(
            UUID id,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection,
            String currentItemHash) {
        super(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                currentItemHash);
    }

    public static HederaNodeResponse fromDomain(HederaNode node, String currentItemHash) {
        return new HederaNodeResponse(
                node.getId(),
                node.getName().value(),
                SubscriptionConfigurationResponse.fromDomain(node.getSubscriptionConfiguration()),
                InteractionConfigurationResponse.fromDomain(node.getInteractionConfiguration()),
                NodeConnectionResponse.fromDomain(node.getConnection()),
                currentItemHash);
    }
}
