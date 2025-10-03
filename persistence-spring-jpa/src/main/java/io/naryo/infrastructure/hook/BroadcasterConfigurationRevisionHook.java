package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.util.serialization.ConfigurationSchemaConverter.rawObjectsFromSchema;

@Component
public class BroadcasterConfigurationRevisionHook
        implements RevisionHook<BroadcasterConfiguration> {

    private final BroadcasterConfigurationEntityRepository repository;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public BroadcasterConfigurationRevisionHook(
            BroadcasterConfigurationEntityRepository repository,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.repository = repository;
        this.schemaRegistry = schemaRegistry;
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

    private void addBroadcasterConfiguration(BroadcasterConfiguration broadcasterConfiguration) {
        BroadcasterConfigurationEntity entity =
                BroadcasterConfigurationEntity.fromDomain(broadcasterConfiguration);
        entity.setAdditionalProperties(
                rawObjectsFromSchema(
                        broadcasterConfiguration,
                        schemaRegistry.getSchema(
                                ConfigurationSchemaType.BROADCASTER,
                                broadcasterConfiguration.getType().getName())));
        repository.save(entity);
    }

    private void removeBroadcasterConfiguration(BroadcasterConfiguration broadcasterConfiguration) {
        repository.deleteById(broadcasterConfiguration.getId());
    }

    private void updateBroadcasterConfiguration(BroadcasterConfiguration broadcasterConfiguration) {
        repository
                .findById(broadcasterConfiguration.getId())
                .ifPresent(
                        filterDocument -> {
                            repository.save(
                                    BroadcasterConfigurationEntity.fromDomain(
                                            broadcasterConfiguration));
                        });
    }
}
