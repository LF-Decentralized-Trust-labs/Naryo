package io.librevents.infrastructure.configuration.source.env.model.common;

import jakarta.validation.constraints.NotBlank;

public record ConnectionEndpointProperties(@NotBlank String url) {}
