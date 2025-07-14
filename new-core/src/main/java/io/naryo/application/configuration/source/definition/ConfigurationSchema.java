package io.naryo.application.configuration.source.definition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ConfigurationSchema(String type, List<FieldDefinition> fields) {

    Map<String, FieldDefinition> asMap() {
        return fields.stream().collect(Collectors.toMap(FieldDefinition::name, f -> f));
    }
}
