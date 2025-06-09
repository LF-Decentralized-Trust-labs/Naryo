package io.naryo.domain.node.interaction.block.ethereum;

import io.naryo.domain.node.interaction.block.BlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.InteractionMode;

public final class EthereumRpcBlockInteractionConfiguration extends BlockInteractionConfiguration {
    public EthereumRpcBlockInteractionConfiguration() {
        super(InteractionMode.ETHEREUM_RPC);
    }
}
