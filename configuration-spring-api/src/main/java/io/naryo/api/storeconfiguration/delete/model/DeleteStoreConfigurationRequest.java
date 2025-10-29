package io.naryo.api.storeconfiguration.delete.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Delete store configuration request")
public record DeleteStoreConfigurationRequest(@NotBlank String prevItemHash) {}
