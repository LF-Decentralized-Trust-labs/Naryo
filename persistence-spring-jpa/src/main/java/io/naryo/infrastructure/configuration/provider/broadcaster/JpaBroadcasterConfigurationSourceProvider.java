package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@Component
public class JpaBroadcasterConfigurationSourceProvider
        implements BroadcasterConfigurationSourceProvider {

    private final BroadcasterConfigurationEntityRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public JpaBroadcasterConfigurationSourceProvider(
            BroadcasterConfigurationEntityRepository broadcasterConfigurationRepository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = broadcasterConfigurationRepository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public Collection<BroadcasterConfigurationDescriptor> load() {
        List<BroadcasterConfigurationEntity> broadcasterConfigurationEntities =
                this.repository.findAll();
        return new HashSet<>(
                broadcasterConfigurationEntities.stream().map(this::fromEntity).toList());
    }

    @Override
    public int priority() {
        return 0;
    }

    private BroadcasterConfigurationDescriptor fromEntity(
            BroadcasterConfigurationDescriptor entity) {
        ConfigurationSchema schema =
                schemaRegistry.getSchema(
                        ConfigurationSchemaType.BROADCASTER, entity.getType().getName());

        entity.setAdditionalProperties(
                rawObjectsToSchema(entity.getAdditionalProperties(), schema));

        return entity;
    }
}
