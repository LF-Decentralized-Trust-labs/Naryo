package io.naryo.infrastructure.configuration.source.env.serialization.event.store;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.infrastructure.configuration.source.env.model.event.store.EventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.EventStoreTargetProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.database.DatabaseBlockEventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.server.ServerEventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public final class EventStoreConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<EventStoreConfigurationProperties> {

    private static final String PREFIX_EVENT_STORE_SCHEMA = "event_store_";
    private final ConfigurationSchemaRegistry schemaRegistry;

    public EventStoreConfigurationPropertiesDeserializer(
            ConfigurationSchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public EventStoreConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        UUID nodeId = getUuidOrNull(getTextOrNull(root.get("nodeId")));
        EventStoreType type = safeTreeToValue(root, "type", codec, EventStoreType.class);
        EventStoreStrategy strategy =
                safeTreeToValue(root, "strategy", codec, EventStoreStrategy.class);

        return switch (strategy) {
            case BLOCK_BASED -> {
                List<EventStoreTargetProperties> targets =
                        safeTreeToList(root, "targets", codec, EventStoreTargetProperties.class);

                Set<String> knownFields = new HashSet<>(Set.of("type", "strategy", "target"));

                yield switch (type) {
                    case SERVER -> {
                        String serverType = getTextOrNull(root.get("serverType"));
                        ConfigurationSchema schema =
                                schemaRegistry.getSchema(
                                        PREFIX_EVENT_STORE_SCHEMA + serverType.toLowerCase());
                        knownFields.add("serverType");
                        yield new ServerEventStoreConfigurationProperties(
                                nodeId,
                                new HashSet<>(targets),
                                getAdditionalConfiguration(root, knownFields, codec, schema),
                                schema,
                                () -> serverType);
                    }
                    case DATABASE -> {
                        String databaseEngine = getTextOrNull(root.get("databaseEngine"));
                        ConfigurationSchema schema =
                                schemaRegistry.getSchema(
                                        PREFIX_EVENT_STORE_SCHEMA + databaseEngine.toLowerCase());
                        knownFields.add("databaseEngine");
                        yield new DatabaseBlockEventStoreConfigurationProperties(
                                nodeId,
                                new HashSet<>(targets),
                                getAdditionalConfiguration(root, knownFields, codec, schema),
                                schema,
                                () -> databaseEngine);
                    }
                };
            }
        };
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
