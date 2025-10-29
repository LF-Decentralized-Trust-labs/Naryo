package io.naryo.api.node.update.model;

import io.naryo.api.node.common.request.NodeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Update node request")
public record UpdateNodeRequest(@NotNull NodeRequest node, @NotBlank String prevItemHash) {}
