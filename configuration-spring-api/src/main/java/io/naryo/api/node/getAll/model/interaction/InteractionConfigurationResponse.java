package io.naryo.api.node.getAll.model.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.BlockInteractionConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = HederaMirrorNodeBlockInteractionConfigurationResponse.class,
            name = "BLOCK_HEDERA_MIRROR_NODE"),
    @JsonSubTypes.Type(
            value = EthereumRpcBlockInteractionConfigurationResponse.class,
            name = "BLOCK_ETHEREUM_RPC")
})
@Schema(
        description = "Base class for interaction configuration",
        discriminatorProperty = "mode",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "BLOCK_HEDERA_MIRROR_NODE",
                    schema = HederaMirrorNodeBlockInteractionConfigurationResponse.class),
            @DiscriminatorMapping(
                    value = "BLOCK_ETHEREUM_RPC",
                    schema = EthereumRpcBlockInteractionConfigurationResponse.class)
        })
@Getter
public abstract class InteractionConfigurationResponse {

    public static InteractionConfigurationResponse fromDomain(
            InteractionConfiguration interactionConfiguration) {
        return switch (interactionConfiguration) {
            case BlockInteractionConfiguration block ->
                    BlockInteractionConfigurationResponse.fromDomain(block);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + interactionConfiguration);
        };
    }
}
