package io.naryo.infrastructure.configuration.provider.event.store;

import java.util.*;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.eventstore.server.ServerType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.EventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.BlockEventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.server.ServerEventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.event.store.EventStorePropertiesDocumentRepository;
import io.naryo.infrastructure.util.serialization.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public final class MongoEventStoreSourceProvider implements EventStoreSourceProvider {

    private static final String PREFIX_EVENT_STORE_SCHEMA = "event_store_";

    private final EventStorePropertiesDocumentRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoEventStoreSourceProvider(
        EventStorePropertiesDocumentRepository repository,
        ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public Collection<EventStoreConfigurationDescriptor> load() {
        List<EventStoreConfigurationPropertiesDocument> eventStores = this.repository.findAll();
        return new HashSet<>(
            eventStores.stream()
                .map(
                    eventStore ->
                        fromDocument(
                            (BlockEventStoreConfigurationPropertiesDocument)
                                eventStore))
                .toList());
    }

    @Override
    public int priority() {
        return 0;
    }

    private EventStoreConfigurationDescriptor fromDocument(
        BlockEventStoreConfigurationPropertiesDocument document) {
        return switch (document) {
            case ServerEventStoreConfigurationPropertiesDocument serverEventStore -> {
                ServerType serverType = serverEventStore.getServerType();
                ConfigurationSchema schema =
                    schemaRegistry.getSchema(PREFIX_EVENT_STORE_SCHEMA + serverType.getName());
                yield new ServerEventStoreConfigurationPropertiesDocument(
                    serverEventStore.getNodeId().toString(),
                    serverEventStore.getTargets(),
                    getAdditionalConfiguration(serverEventStore.getAdditionalProperties(), schema),
                    serverEventStore.getPropertiesSchema().isPresent()
                        ? ConfigurationSchemaDocument.toDocument(
                        serverEventStore.getPropertiesSchema().get())
                        : null,
                    serverEventStore.getServerType().getName());
            }
            default -> throw new IllegalStateException("Unexpected value: " + document);
        };
    }

    private Map<String, Object> getAdditionalConfiguration(
        Map<String, Object> additionalProperties, ConfigurationSchema schema) {
        Map<String, Object> additionalConfiguration = new HashMap<>();

        if (!additionalProperties.isEmpty()) {
            for (Map.Entry<String, Object> entry : additionalProperties.entrySet()) {
                Optional<FieldDefinition> field =
                    schema.fields().stream()
                        .filter(f -> f.name().equals(entry.getKey()))
                        .findFirst();
                if (field.isPresent()) {
                    Object value = TypeConverter.castToType(entry.getValue(), field.get().type());
                    additionalConfiguration.put(
                        entry.getKey(), value != null ? value : field.get().defaultValue());
                }
            }
        }

        return additionalConfiguration;
    }

}
