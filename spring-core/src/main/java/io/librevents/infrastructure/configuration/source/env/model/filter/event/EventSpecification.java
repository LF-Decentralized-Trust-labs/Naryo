package io.librevents.infrastructure.configuration.source.env.model.filter.event;

public record EventSpecification(String signature, CorrelationId correlationId) {}
