package io.librevents.infrastructure.configuration.source.env.model.filter;

import java.util.UUID;

import io.librevents.domain.filter.FilterType;

public record FilterProperties(
        UUID id,
        String name,
        FilterType type,
        UUID nodeId,
        FilterConfigurationProperties configuration) {}
