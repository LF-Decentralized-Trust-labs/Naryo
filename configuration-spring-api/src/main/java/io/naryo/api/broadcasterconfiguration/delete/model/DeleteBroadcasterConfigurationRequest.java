package io.naryo.api.broadcasterconfiguration.delete.model;

import jakarta.validation.constraints.NotBlank;

public record DeleteBroadcasterConfigurationRequest(@NotBlank String prevItemHash) {}
