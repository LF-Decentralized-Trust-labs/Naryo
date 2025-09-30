package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.store.StoreConfigurationEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class StoreRevisionHook implements RevisionHook<StoreConfiguration> {

    private final StoreConfigurationEntityRepository repository;

    public StoreRevisionHook(StoreConfigurationEntityRepository repository) {
        this.repository = repository;
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
        this.repository.save(StoreConfigurationEntity.fromDomain(source));
    }

    private void removeStore(StoreConfiguration source) {
        repository.deleteById(source.getNodeId());
    }

    private void updateStore(StoreConfiguration source) {
        repository.findById(source.getNodeId()).ifPresent(repository::save);
    }
}
