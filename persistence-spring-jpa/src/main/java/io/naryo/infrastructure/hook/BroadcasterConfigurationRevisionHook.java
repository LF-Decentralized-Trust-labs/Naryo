package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterConfigurationRevisionHook implements RevisionHook<BroadcasterConfigurationDescriptor> {

    private final BroadcasterConfigurationEntityRepository repository;

    public BroadcasterConfigurationRevisionHook(BroadcasterConfigurationEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<BroadcasterConfigurationDescriptor> diff) {
        for (BroadcasterConfigurationDescriptor add : diff.added()) {
            addBroadcasterConfiguration(add);
        }
        for (BroadcasterConfigurationDescriptor remove : diff.removed()) {
            removeBroadcasterConfiguration(remove);
        }
        for (DiffResult.Modified<BroadcasterConfigurationDescriptor> modified : diff.modified()) {
            updateBroadcasterConfiguration(modified.after());
        }
    }

    @Override
    public void onAfterApply(Revision<BroadcasterConfigurationDescriptor> applied, DiffResult<BroadcasterConfigurationDescriptor> diff) {}

    private void addBroadcasterConfiguration(BroadcasterConfigurationDescriptor descriptor) {
        repository.save(BroadcasterConfigurationEntity.fromDescriptor(descriptor));
    }

    private void removeBroadcasterConfiguration(BroadcasterConfigurationDescriptor descriptor) {
        repository.deleteById(descriptor.getId());
    }

    private void updateBroadcasterConfiguration(BroadcasterConfigurationDescriptor descriptor) {
        repository.findById(descriptor.getId()).ifPresent(repository::save);
    }
}
