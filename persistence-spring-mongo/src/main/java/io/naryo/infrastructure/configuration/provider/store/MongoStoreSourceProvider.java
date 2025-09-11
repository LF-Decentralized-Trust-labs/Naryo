package io.naryo.infrastructure.configuration.provider.store;

import java.util.*;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.provider.StoreSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.ActiveStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.InactiveStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.store.StorePropertiesDocumentRepository;
import io.naryo.infrastructure.util.serialization.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public final class MongoStoreSourceProvider implements StoreSourceProvider {

    private final StorePropertiesDocumentRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoStoreSourceProvider(
            StorePropertiesDocumentRepository repository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public Collection<StoreConfigurationDescriptor> load() {
        List<StoreConfigurationPropertiesDocument> eventStores = this.repository.findAll();
        return new HashSet<>(eventStores.stream().map(this::fromDocument).toList());
    }

    @Override
    public int priority() {
        return 0;
    }

    private StoreConfigurationDescriptor fromDocument(
            StoreConfigurationPropertiesDocument document) {
        return switch (document) {
            case ActiveStoreConfigurationPropertiesDocument active -> {
                ConfigurationSchema schema =
                        schemaRegistry.getSchema(
                                ConfigurationSchemaType.STORE, active.getType().getName());
                yield new ActiveStoreConfigurationPropertiesDocument(
                        active.getNodeId().toString(),
                        active.getType().getName(),
                        active.getFeatures(),
                        getAdditionalConfiguration(active.getAdditionalProperties(), schema),
                        ConfigurationSchemaDocument.toDocument(schema));
            }
            case InactiveStoreConfigurationPropertiesDocument inactive -> inactive;
            default -> throw new IllegalStateException("Unexpected value: " + document);
        };
    }

    private Map<String, Object> getAdditionalConfiguration(
            Map<String, Object> additionalProperties, ConfigurationSchema schema) {
        Map<String, Object> additionalConfiguration = new HashMap<>();

        if (!additionalProperties.isEmpty() && schema != null) {
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
