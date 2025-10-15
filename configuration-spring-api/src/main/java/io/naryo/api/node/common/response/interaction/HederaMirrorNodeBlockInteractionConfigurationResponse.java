package io.naryo.api.node.common.response.interaction;

import lombok.Builder;

@Builder
public record HederaMirrorNodeBlockInteractionConfigurationResponse(
        String strategy, String mode, int limitPerRequest, int retriesPerRequest)
        implements InteractionConfigurationResponse {}
