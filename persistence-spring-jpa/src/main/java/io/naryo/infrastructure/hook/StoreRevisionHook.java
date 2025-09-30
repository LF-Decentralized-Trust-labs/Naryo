package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.store.StoreConfigurationEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class StoreRevisionHook implements RevisionHook<StoreConfigurationDescriptor> {

    private final StoreConfigurationEntityRepository repository;

    public StoreRevisionHook(StoreConfigurationEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<StoreConfigurationDescriptor> diff) {
        for (StoreConfigurationDescriptor add : diff.added()) {
            addStore(add);
        }
        for (StoreConfigurationDescriptor remove : diff.removed()) {
            removeStore(remove);
        }
        for (DiffResult.Modified<StoreConfigurationDescriptor> modified : diff.modified()) {
            updateStore(modified.after());
        }
    }

    @Override
    public void onAfterApply(
            Revision<StoreConfigurationDescriptor> applied,
            DiffResult<StoreConfigurationDescriptor> diff) {}

    private void addStore(StoreConfigurationDescriptor descriptor) {
        this.repository.save(StoreConfigurationEntity.fromDescriptor(descriptor));
    }

    private void removeStore(StoreConfigurationDescriptor descriptor) {
        repository.deleteById(descriptor.getNodeId());
    }

    private void updateStore(StoreConfigurationDescriptor descriptor) {
        repository.findById(descriptor.getNodeId()).ifPresent(repository::save);
    }
}
