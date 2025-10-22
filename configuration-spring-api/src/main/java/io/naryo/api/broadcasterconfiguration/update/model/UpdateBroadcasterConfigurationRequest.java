package io.naryo.api.broadcasterconfiguration.update.model;

import io.naryo.api.broadcasterconfiguration.common.request.BroadcasterConfigurationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBroadcasterConfigurationRequest(
        @NotNull BroadcasterConfigurationRequest broadcasterConfig,
        @NotBlank String prevItemHash) {}
