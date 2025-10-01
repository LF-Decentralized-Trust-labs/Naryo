package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.store.ActiveStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.store.StorePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsFromSchema;

@Component
public class StoreConfigurationPropertiesDocumentRevisionHook
        implements RevisionHook<StoreConfiguration> {

    private final StorePropertiesDocumentRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public StoreConfigurationPropertiesDocumentRevisionHook(
            StorePropertiesDocumentRepository repository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void onBeforeApply(DiffResult<StoreConfiguration> diff) {
        for (StoreConfiguration add : diff.added()) {
            addStore(add);
        }
        for (StoreConfiguration remove : diff.removed()) {
            removeStore(remove);
        }
        for (DiffResult.Modified<StoreConfiguration> modified : diff.modified()) {
            updateStore(modified.after());
        }
    }

    @Override
    public void onAfterApply(
            Revision<StoreConfiguration> applied, DiffResult<StoreConfiguration> diff) {}

    private void addStore(StoreConfiguration source) {
        StoreConfigurationPropertiesDocument entity =
                StoreConfigurationPropertiesDocument.fromDomain(source);
        if (entity instanceof ActiveStoreConfigurationPropertiesDocument activeEntity) {
            activeEntity.setAdditionalProperties(
                    rawObjectsFromSchema(
                            activeEntity,
                            schemaRegistry.getSchema(
                                    ConfigurationSchemaType.STORE,
                                    activeEntity.getType().getName())));
        }
        this.repository.save(StoreConfigurationPropertiesDocument.fromDomain(source));
    }

    private void removeStore(StoreConfiguration source) {
        repository.deleteById(source.getNodeId().toString());
    }

    private void updateStore(StoreConfiguration source) {
        repository.findById(source.getNodeId().toString()).ifPresent(repository::save);
    }
}
