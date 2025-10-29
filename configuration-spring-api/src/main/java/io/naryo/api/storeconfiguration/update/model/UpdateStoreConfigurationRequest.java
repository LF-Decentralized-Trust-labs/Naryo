package io.naryo.api.storeconfiguration.update.model;

import io.naryo.api.storeconfiguration.common.request.StoreConfigurationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Update store configuration request")
public record UpdateStoreConfigurationRequest(
        @NotNull StoreConfigurationRequest target, @NotBlank String prevItemHash) {}
