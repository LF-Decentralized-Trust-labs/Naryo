package io.naryo.api.node.common.response.interaction;

import lombok.Builder;

@Builder
public record EthereumRpcBlockInteractionConfigurationResponse(String strategy, String mode)
        implements InteractionConfigurationResponse {}
