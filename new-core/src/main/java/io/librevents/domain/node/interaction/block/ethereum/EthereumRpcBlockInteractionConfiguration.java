package io.librevents.domain.node.interaction.block.ethereum;

import io.librevents.domain.node.interaction.block.BlockInteractionConfiguration;
import io.librevents.domain.node.interaction.block.InteractionMode;

public final class EthereumRpcBlockInteractionConfiguration extends BlockInteractionConfiguration {
    public EthereumRpcBlockInteractionConfiguration() {
        super(InteractionMode.ETHEREUM_RPC);
    }
}
