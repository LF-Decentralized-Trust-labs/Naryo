package io.naryo.api.broadcasterconfiguration.update.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBroadcasterConfigurationRequest(
        @NotNull UpdateBroadcasterConfigurationRequestTarget target, @NotBlank String prevItemHash) {}
