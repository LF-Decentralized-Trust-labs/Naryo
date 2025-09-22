package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationDocumentRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsToSchema;

@Component
public class MongoBroadcasterConfigurationSourceProvider
        implements BroadcasterConfigurationSourceProvider {

    private final BroadcasterConfigurationDocumentRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoBroadcasterConfigurationSourceProvider(
            BroadcasterConfigurationDocumentRepository repository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public Collection<BroadcasterConfigurationDescriptor> load() {
        List<BroadcasterConfigurationDocument> broadcasterConfigurations =
                this.repository.findAll();
        return new HashSet<>(broadcasterConfigurations.stream().map(this::fromDocument).toList());
    }

    @Override
    public int priority() {
        return 0;
    }

    private BroadcasterConfigurationDescriptor fromDocument(
            BroadcasterConfigurationDocument document) {
        ConfigurationSchema schema =
                schemaRegistry.getSchema(
                        ConfigurationSchemaType.BROADCASTER, document.getType().getName());

        document.setAdditionalProperties(
                rawObjectsToSchema(document.getAdditionalProperties(), schema));

        return document;
    }
}
