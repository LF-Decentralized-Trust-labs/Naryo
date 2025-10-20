package io.naryo.api.filter.delete.model;

import jakarta.validation.constraints.NotBlank;

public record DeleteFilterRequest(@NotBlank String prevItemHash) {}
