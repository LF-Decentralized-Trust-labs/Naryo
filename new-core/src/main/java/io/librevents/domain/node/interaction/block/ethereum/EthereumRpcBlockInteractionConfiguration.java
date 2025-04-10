package io.librevents.domain.node.interaction.block.ethereum;

import io.librevents.domain.node.interaction.block.BlockInteractionConfiguration;
import io.librevents.domain.node.interaction.block.InteractionMode;

public class EthereumRpcBlockInteractionConfiguration extends BlockInteractionConfiguration {
    protected EthereumRpcBlockInteractionConfiguration() {
        super(InteractionMode.ETHEREUM_RPC);
    }
}
