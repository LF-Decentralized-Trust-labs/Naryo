package io.naryo.infrastructure.configuration.source.env.model.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import io.naryo.domain.node.interaction.block.InteractionMode;

public class EthereumRpcBlockInteractionProperties extends BlockInteractionProperties
        implements EthereumRpcBlockInteractionDescriptor {

    public EthereumRpcBlockInteractionProperties() {
        super(InteractionMode.ETHEREUM_RPC);
    }
}
