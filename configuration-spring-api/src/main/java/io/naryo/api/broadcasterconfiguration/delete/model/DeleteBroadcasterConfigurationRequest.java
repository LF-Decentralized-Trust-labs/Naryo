package io.naryo.api.broadcasterconfiguration.delete.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Delete broadcaster configuration request")
public record DeleteBroadcasterConfigurationRequest(@NotBlank String prevItemHash) {}
