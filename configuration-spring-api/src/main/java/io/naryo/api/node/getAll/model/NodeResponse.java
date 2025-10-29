package io.naryo.api.node.getAll.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.getAll.model.connection.NodeConnectionResponse;
import io.naryo.api.node.getAll.model.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.getAll.model.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicEthereumNodeResponse.class, name = "ETHEREUM_PUBLIC"),
    @JsonSubTypes.Type(value = PrivateEthereumNodeResponse.class, name = "ETHEREUM_PRIVATE"),
    @JsonSubTypes.Type(value = HederaNodeResponse.class, name = "HEDERA"),
})
@Schema(
        description = "Base node",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "ETHEREUM_PUBLIC",
                    schema = PublicEthereumNodeResponse.class),
            @DiscriminatorMapping(
                    value = "ETHEREUM_PRIVATE",
                    schema = PrivateEthereumNodeResponse.class),
            @DiscriminatorMapping(value = "HEDERA", schema = HederaNodeResponse.class)
        })
@Getter
public abstract class NodeResponse {

    protected final UUID id;
    protected final String name;
    protected final SubscriptionConfigurationResponse subscription;
    protected final InteractionConfigurationResponse interaction;
    protected final NodeConnectionResponse connection;
    protected String currentItemHash;

    protected NodeResponse(
            UUID id,
            String name,
            SubscriptionConfigurationResponse subscription,
            InteractionConfigurationResponse interaction,
            NodeConnectionResponse connection,
            String currentItemHash) {
        this.id = id;
        this.name = name;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
        this.currentItemHash = currentItemHash;
    }

    public static NodeResponse fromDomain(Node node, String currentItemHash) {
        return switch (node) {
            case EthereumNode ethereumNode ->
                    EthereumNodeResponse.fromDomain(ethereumNode, currentItemHash);
            case HederaNode hederaNode ->
                    HederaNodeResponse.fromDomain(hederaNode, currentItemHash);
            default -> throw new IllegalStateException("Unexpected value: " + node);
        };
    }
}
