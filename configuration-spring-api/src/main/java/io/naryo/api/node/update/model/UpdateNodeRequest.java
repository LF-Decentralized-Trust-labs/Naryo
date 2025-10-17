package io.naryo.api.node.update.model;

import io.naryo.api.node.common.request.NodeRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateNodeRequest(@NotNull NodeRequest node, @NotBlank String prevItemHash) {}
