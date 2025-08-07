package io.naryo.infrastructure.configuration.source.env.serialization.store;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.source.env.model.store.ActiveStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public final class ActiveStoreConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<ActiveStoreConfigurationProperties> {

    private static final String PREFIX_EVENT_STORE_SCHEMA = "event_store_";
    private final ConfigurationSchemaRegistry schemaRegistry;

    public ActiveStoreConfigurationPropertiesDeserializer(
            ConfigurationSchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public ActiveStoreConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        UUID nodeId = getUuidOrNull(getTextOrNull(root.get("nodeId")));
        StoreType type = safeTreeToValue(root, "type", codec, StoreType.class);

        ConfigurationSchema schema =
                schemaRegistry.getSchema(PREFIX_EVENT_STORE_SCHEMA + type.getName().toLowerCase());
        Set<String> knownFields = Set.of("type", "strategy", "target");
        return new ActiveStoreConfigurationProperties(
                nodeId,
                type,
                getFeatures(root, codec),
                getAdditionalConfiguration(root, knownFields, codec, schema),
                schema);
    }

    private @NotNull Map<StoreFeatureType, StoreFeatureConfigurationProperties> getFeatures(
            JsonNode root, ObjectCodec codec) throws IOException {
        List<StoreFeatureConfigurationProperties> featurePropertiesList =
                safeTreeToList(root, "features", codec, StoreFeatureConfigurationProperties.class);

        return featurePropertiesList.stream()
                .collect(
                        Collectors.toMap(
                                StoreFeatureConfigurationProperties::getType, feature -> feature));
    }

    private static @NotNull Map<String, Object> getAdditionalConfiguration(
            JsonNode root, Set<String> knownFields, ObjectCodec codec, ConfigurationSchema schema)
            throws JsonProcessingException {
        Map<String, Object> additionalConfiguration = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (!knownFields.contains(entry.getKey())) {
                Optional<FieldDefinition> field =
                        schema.fields().stream()
                                .filter(f -> f.name().equals(entry.getKey()))
                                .findFirst();
                if (field.isPresent()) {
                    Object value = codec.treeToValue(entry.getValue(), field.get().type());
                    additionalConfiguration.put(
                            entry.getKey(), value != null ? value : field.get().defaultValue());
                }
            }
        }
        return additionalConfiguration;
    }
}
