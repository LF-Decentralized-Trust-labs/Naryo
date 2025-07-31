package io.naryo.application.configuration.source.definition;

public record FieldDefinition(String name, Class<?> type, boolean required, Object defaultValue) {}
