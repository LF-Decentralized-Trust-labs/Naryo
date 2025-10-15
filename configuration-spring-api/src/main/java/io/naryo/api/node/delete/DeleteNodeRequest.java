package io.naryo.api.node.delete;

import jakarta.validation.constraints.NotBlank;

public record DeleteNodeRequest(@NotBlank String prevItemHash) {}
