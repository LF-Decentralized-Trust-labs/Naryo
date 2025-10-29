package io.naryo.api.filter.delete.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Delete filter request")
public record DeleteFilterRequest(@NotBlank String prevItemHash) {}
