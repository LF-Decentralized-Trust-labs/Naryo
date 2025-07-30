package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.domain.node.interaction.block.InteractionMode;

public interface EthereumRpcBlockInteractionDescriptor extends BlockInteractionDescriptor {

    @Override
    default InteractionMode getMode() {
        return InteractionMode.ETHEREUM_RPC;
    }

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        if (!(other
                instanceof
                EthereumRpcBlockInteractionDescriptor otherEthereumRpcBlockBlockInteraction)) {
            return this;
        }

        return BlockInteractionDescriptor.super.merge(otherEthereumRpcBlockBlockInteraction);
    }
}
