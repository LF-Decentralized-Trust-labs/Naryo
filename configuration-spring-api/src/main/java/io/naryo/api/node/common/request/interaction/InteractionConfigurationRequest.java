package io.naryo.api.node.common.request.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = HederaMirrorNodeBlockInteractionConfigurationRequest.class,
            name = "HEDERA_MIRROR_NODE"),
    @JsonSubTypes.Type(
            value = EthereumRpcBlockInteractionConfigurationRequest.class,
            name = "ETHEREUM_RPC")
})
public abstract class InteractionConfigurationRequest {

    private final @NotNull InteractionStrategy strategy;
    private final @NotNull InteractionMode mode;

    protected InteractionConfigurationRequest(InteractionMode mode) {
        this.strategy = InteractionStrategy.BLOCK_BASED;
        this.mode = mode;
    }

    public InteractionConfiguration toDomain() {
        return switch (mode) {
            case HEDERA_MIRROR_NODE ->
                    ((HederaMirrorNodeBlockInteractionConfigurationRequest) this).toDomain();
            case ETHEREUM_RPC -> new EthereumRpcBlockInteractionConfiguration();
        };
    }
}
