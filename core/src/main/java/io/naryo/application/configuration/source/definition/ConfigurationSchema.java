package io.naryo.application.configuration.source.definition;

import java.util.List;

public record ConfigurationSchema(String type, List<FieldDefinition> fields) {}
