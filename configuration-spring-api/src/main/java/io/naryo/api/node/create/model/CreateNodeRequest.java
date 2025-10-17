package io.naryo.api.node.create.model;

import io.naryo.api.node.common.request.NodeRequest;
import jakarta.validation.constraints.NotNull;

public record CreateNodeRequest(@NotNull NodeRequest node) {}
