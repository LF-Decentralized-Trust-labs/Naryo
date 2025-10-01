package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationDocumentRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsFromSchema;

@Component
public class BroadcasterConfigurationDocumentRevisionHook
        implements RevisionHook<BroadcasterConfiguration> {

    private final BroadcasterConfigurationDocumentRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public BroadcasterConfigurationDocumentRevisionHook(
            BroadcasterConfigurationDocumentRepository repository,
            ConfigurationSchemaRegistry registry) {
        this.repository = repository;
        this.schemaRegistry = registry;
    }

    @Override
    public void onBeforeApply(DiffResult<BroadcasterConfiguration> diff) {
        for (BroadcasterConfiguration add : diff.added()) {
            addBroadcasterConfiguration(add);
        }
        for (BroadcasterConfiguration remove : diff.removed()) {
            removeBroadcasterConfiguration(remove);
        }
        for (DiffResult.Modified<BroadcasterConfiguration> modified : diff.modified()) {
            updateBroadcasterConfiguration(modified.after());
        }
    }

    @Override
    public void onAfterApply(
            Revision<BroadcasterConfiguration> applied,
            DiffResult<BroadcasterConfiguration> diff) {}

    private void addBroadcasterConfiguration(BroadcasterConfiguration source) {
        BroadcasterConfigurationDocument entity =
                BroadcasterConfigurationDocument.fromDomain(source);
        entity.setAdditionalProperties(
                rawObjectsFromSchema(
                        source,
                        schemaRegistry.getSchema(
                                ConfigurationSchemaType.BROADCASTER, source.getType().getName())));
        repository.save(entity);
    }

    private void removeBroadcasterConfiguration(BroadcasterConfiguration source) {
        repository.deleteById(source.getId().toString());
    }

    private void updateBroadcasterConfiguration(BroadcasterConfiguration source) {
        repository.findById(source.getId().toString()).ifPresent(repository::save);
    }
}
