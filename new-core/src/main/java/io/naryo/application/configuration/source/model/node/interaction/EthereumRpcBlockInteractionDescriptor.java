package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.domain.node.interaction.block.InteractionMode;

public interface EthereumRpcBlockInteractionDescriptor extends BlockInteractionDescriptor {

    @Override
    default InteractionMode getMode() {
        return InteractionMode.ETHEREUM_RPC;
    }
}
