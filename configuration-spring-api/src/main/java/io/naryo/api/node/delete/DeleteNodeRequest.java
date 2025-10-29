package io.naryo.api.node.delete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Delete node request")
public record DeleteNodeRequest(@NotBlank String prevItemHash) {}
