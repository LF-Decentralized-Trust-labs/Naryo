package io.naryo.api.filter.update.model;

import io.naryo.api.filter.common.request.FilterRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFilterRequest(@NotNull FilterRequest target, @NotBlank String prevItemHash) {}
