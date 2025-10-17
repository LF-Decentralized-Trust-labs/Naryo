package io.naryo.api.storeconfiguration.update.model;

import io.naryo.api.storeconfiguration.common.request.StoreConfigurationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStoreConfigurationRequest(
        @NotNull StoreConfigurationRequest target, @NotBlank String prevItemHash) {}
