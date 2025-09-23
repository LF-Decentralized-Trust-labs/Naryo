package io.naryo.domain.node.interaction.block;

import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;

public final class EthereumRpcBlockInteractionConfigurationBuilder
        extends BlockInteractionConfigurationBuilder<
                EthereumRpcBlockInteractionConfigurationBuilder,
                EthereumRpcBlockInteractionConfiguration> {

    @Override
    public EthereumRpcBlockInteractionConfigurationBuilder self() {
        return this;
    }

    @Override
    public EthereumRpcBlockInteractionConfiguration build() {
        return new EthereumRpcBlockInteractionConfiguration();
    }
}
