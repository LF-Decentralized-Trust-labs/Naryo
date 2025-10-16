package io.naryo.api.node.common.request.interaction;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;

public final class EthereumRpcBlockInteractionConfigurationRequest
        extends InteractionConfigurationRequest {

    public EthereumRpcBlockInteractionConfigurationRequest() {
        super(InteractionMode.ETHEREUM_RPC);
    }

    @Override
    public InteractionConfiguration toDomain() {
        return new EthereumRpcBlockInteractionConfiguration();
    }
}
