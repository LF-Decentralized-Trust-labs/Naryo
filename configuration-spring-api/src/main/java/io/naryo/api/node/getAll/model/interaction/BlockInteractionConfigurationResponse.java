package io.naryo.api.node.getAll.model.interaction;

import io.naryo.domain.node.interaction.block.BlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Base class for block interaction configuration")
@Getter
public abstract class BlockInteractionConfigurationResponse
        extends InteractionConfigurationResponse {

    public static BlockInteractionConfigurationResponse fromDomain(
            BlockInteractionConfiguration blockInteractionConfiguration) {
        return switch (blockInteractionConfiguration) {
            case EthereumRpcBlockInteractionConfiguration ignored ->
                    EthereumRpcBlockInteractionConfigurationResponse.fromDomain();
            case HederaMirrorNodeBlockInteractionConfiguration hedera ->
                    HederaMirrorNodeBlockInteractionConfigurationResponse.fromDomain(hedera);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + blockInteractionConfiguration);
        };
    }
}
