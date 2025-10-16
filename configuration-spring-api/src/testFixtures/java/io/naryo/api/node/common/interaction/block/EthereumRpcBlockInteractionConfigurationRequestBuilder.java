package io.naryo.api.node.common.interaction.block;

import io.naryo.api.node.common.interaction.InteractionConfigurationRequestBuilder;
import io.naryo.api.node.common.request.interaction.EthereumRpcBlockInteractionConfigurationRequest;

public final class EthereumRpcBlockInteractionConfigurationRequestBuilder
        extends InteractionConfigurationRequestBuilder<
                EthereumRpcBlockInteractionConfigurationRequestBuilder,
                EthereumRpcBlockInteractionConfigurationRequest> {

    @Override
    public EthereumRpcBlockInteractionConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public EthereumRpcBlockInteractionConfigurationRequest build() {
        return new EthereumRpcBlockInteractionConfigurationRequest();
    }
}
