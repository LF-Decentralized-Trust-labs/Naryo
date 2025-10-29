package io.naryo.api.broadcaster.delete.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Delete broadcaster request")
public record DeleteBroadcasterRequest(@NotBlank String prevItemHash) {}
