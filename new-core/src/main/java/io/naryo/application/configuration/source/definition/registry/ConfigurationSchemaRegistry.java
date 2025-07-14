package io.naryo.application.configuration.source.definition.registry;

import java.util.HashMap;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;

public final class ConfigurationSchemaRegistry {

    private final Map<String, ConfigurationSchema> schemas;

    private ConfigurationSchemaRegistry(Map<String, ConfigurationSchema> schemas) {
        this.schemas = new HashMap<>(schemas);
    }

    public ConfigurationSchemaRegistry() {
        this(Map.of());
    }

    public ConfigurationSchema getSchema(String type) {
        return schemas.get(type.toLowerCase());
    }

    public void register(String type, ConfigurationSchema schema) {
        schemas.put(type.toLowerCase(), schema);
    }
}
