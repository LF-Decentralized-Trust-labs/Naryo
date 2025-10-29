package io.naryo.api.broadcasterconfiguration.update.model;

import io.naryo.api.broadcasterconfiguration.common.request.BroadcasterConfigurationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Update broadcaster configuration request")
public record UpdateBroadcasterConfigurationRequest(
        @NotNull BroadcasterConfigurationRequest broadcasterConfig,
        @NotBlank String prevItemHash) {}
