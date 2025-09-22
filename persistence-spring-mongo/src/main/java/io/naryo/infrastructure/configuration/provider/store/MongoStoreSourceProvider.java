package io.naryo.infrastructure.configuration.provider.store;

import java.util.*;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.store.configuration.provider.StoreSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.store.ActiveStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.InactiveStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.store.StorePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

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
                active.setAdditionalProperties(
                        rawObjectsToSchema(active.getAdditionalProperties(), schema));
                yield active;
            }
            case InactiveStoreConfigurationPropertiesDocument inactive -> inactive;
            default -> throw new IllegalStateException("Unexpected value: " + document);
        };
    }
}
