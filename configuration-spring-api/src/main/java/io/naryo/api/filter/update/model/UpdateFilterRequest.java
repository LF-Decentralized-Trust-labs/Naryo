package io.naryo.api.filter.update.model;

import io.naryo.api.filter.common.request.FilterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Update filter request")
public record UpdateFilterRequest(@NotNull FilterRequest filter, @NotBlank String prevItemHash) {}
