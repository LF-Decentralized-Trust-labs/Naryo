package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.store.ActiveStoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.store.StoreConfigurationEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsFromSchema;

@Slf4j
@Component
public class StoreRevisionHook implements RevisionHook<StoreConfiguration> {

    private final StoreConfigurationEntityRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public StoreRevisionHook(
            StoreConfigurationEntityRepository repository,
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

    private void addStore(StoreConfiguration storeConfiguration) {
        StoreConfigurationEntity entity = StoreConfigurationEntity.fromDomain(storeConfiguration);
        if (entity instanceof ActiveStoreConfigurationEntity activeEntity) {
            activeEntity.setAdditionalProperties(
                    rawObjectsFromSchema(
                            activeEntity,
                            schemaRegistry.getSchema(
                                    ConfigurationSchemaType.STORE,
                                    activeEntity.getType().getName())));
        }
        this.repository.save(StoreConfigurationEntity.fromDomain(storeConfiguration));
    }

    private void removeStore(StoreConfiguration storeConfiguration) {
        repository.deleteById(storeConfiguration.getNodeId());
    }

    private void updateStore(StoreConfiguration storeConfiguration) {
        repository
                .findById(storeConfiguration.getNodeId())
                .ifPresent(
                        filterDocument -> {
                            repository.save(
                                    StoreConfigurationEntity.fromDomain(storeConfiguration));
                        });
    }
}
