package io.naryo.api.node.common.request.interaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Base block interaction configuration request")
@Getter
public abstract class BlockInteractionConfigurationRequest
        extends InteractionConfigurationRequest {}
