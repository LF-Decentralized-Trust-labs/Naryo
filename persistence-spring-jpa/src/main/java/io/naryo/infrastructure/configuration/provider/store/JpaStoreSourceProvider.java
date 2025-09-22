package io.naryo.infrastructure.configuration.provider.store;

import java.util.*;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.store.configuration.provider.StoreSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.store.ActiveStoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.entity.store.InactiveStoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.store.StoreConfigurationEntityRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@Component
public class JpaStoreSourceProvider implements StoreSourceProvider {

    private final StoreConfigurationEntityRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public JpaStoreSourceProvider(
            StoreConfigurationEntityRepository repository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public Collection<StoreConfigurationDescriptor> load() {
        List<StoreConfigurationEntity> eventStores = this.repository.findAll();
        return new HashSet<>(eventStores.stream().map(this::fromEntity).toList());
    }

    @Override
    public int priority() {
        return 0;
    }

    private StoreConfigurationDescriptor fromEntity(StoreConfigurationEntity entity) {
        return switch (entity) {
            case ActiveStoreConfigurationEntity active -> {
                ConfigurationSchema schema =
                        schemaRegistry.getSchema(
                                ConfigurationSchemaType.STORE, active.getType().getName());
                active.setAdditionalProperties(
                        rawObjectsToSchema(active.getAdditionalProperties(), schema));
                yield active;
            }
            case InactiveStoreConfigurationEntity inactive -> inactive;
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        };
    }
}
