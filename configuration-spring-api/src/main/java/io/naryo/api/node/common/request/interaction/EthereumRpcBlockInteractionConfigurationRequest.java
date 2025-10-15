package io.naryo.api.node.common.request.interaction;

import io.naryo.domain.node.interaction.block.InteractionMode;

public final class EthereumRpcBlockInteractionConfigurationRequest
        extends InteractionConfigurationRequest {

    public EthereumRpcBlockInteractionConfigurationRequest() {
        super(InteractionMode.ETHEREUM_RPC);
    }
}
