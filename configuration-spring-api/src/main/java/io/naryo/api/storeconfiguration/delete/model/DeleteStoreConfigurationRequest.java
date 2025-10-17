package io.naryo.api.storeconfiguration.delete.model;

import jakarta.validation.constraints.NotBlank;

public record DeleteStoreConfigurationRequest(@NotBlank String prevItemHash) {}
