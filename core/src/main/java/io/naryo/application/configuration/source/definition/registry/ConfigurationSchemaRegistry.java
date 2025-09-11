package io.naryo.application.configuration.source.definition.registry;

import java.util.HashMap;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;

public final class ConfigurationSchemaRegistry {

    private final Map<String, ConfigurationSchema> schemas;

    public ConfigurationSchemaRegistry() {
        this.schemas = new HashMap<>();
    }

    public ConfigurationSchema getSchema(ConfigurationSchemaType schemaType, String suffix) {
        return schemas.get(getConfigurationSchemaKey(schemaType, suffix));
    }

    public void register(ConfigurationSchemaType type, String suffix, ConfigurationSchema schema) {
        schemas.put(getConfigurationSchemaKey(type, suffix), schema);
    }

    private String getConfigurationSchemaKey(ConfigurationSchemaType type, String suffix) {
        return (type + "_" + suffix).toLowerCase();
    }
}
