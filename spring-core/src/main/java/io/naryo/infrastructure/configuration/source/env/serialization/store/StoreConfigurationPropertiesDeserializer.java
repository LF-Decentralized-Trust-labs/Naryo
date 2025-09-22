package io.naryo.infrastructure.configuration.source.env.serialization.store;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.source.env.model.store.ActiveStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.configuration.source.env.serialization.utils.EnvSerializationUtils.jsonNodesToObjects;

@Component
public final class StoreConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<StoreConfigurationProperties> {

    private final ConfigurationSchemaRegistry schemaRegistry;

    public StoreConfigurationPropertiesDeserializer(ConfigurationSchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public StoreConfigurationProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        UUID nodeId = getUuidOrNull(getTextOrNull(root.get("nodeId")));
        String type = getTextOrNull(root.get("type"));

        ConfigurationSchema schema = schemaRegistry.getSchema(ConfigurationSchemaType.STORE, type);
        Set<String> knownFields = Set.of("type", "strategy", "targets");
        Map<String, Object> additionalConfiguration =
                jsonNodesToObjects(codec, root.fields(), knownFields, schema);

        return new ActiveStoreConfigurationProperties(
                nodeId, () -> type, getFeatures(root, codec), additionalConfiguration);
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
}
