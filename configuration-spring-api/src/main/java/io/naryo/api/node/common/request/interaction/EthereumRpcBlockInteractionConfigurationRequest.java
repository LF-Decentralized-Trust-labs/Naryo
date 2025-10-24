package io.naryo.api.node.common.request.interaction;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Ethereum interaction configuration")
@Getter
public final class EthereumRpcBlockInteractionConfigurationRequest
        extends BlockInteractionConfigurationRequest {

    public EthereumRpcBlockInteractionConfigurationRequest() {}

    @Override
    public InteractionConfiguration toDomain() {
        return new EthereumRpcBlockInteractionConfiguration();
    }
}
