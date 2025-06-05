package io.librevents.infrastructure.configuration.source.env.model.filter;

import java.util.UUID;

import io.librevents.domain.filter.FilterType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilterProperties(
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull FilterType type,
        @NotNull UUID nodeId,
        @Valid @NotNull FilterConfigurationProperties configuration) {}
