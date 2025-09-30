package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterConfigurationRevisionHook
        implements RevisionHook<BroadcasterConfiguration> {

    private final BroadcasterConfigurationEntityRepository repository;

    public BroadcasterConfigurationRevisionHook(
            BroadcasterConfigurationEntityRepository repository) {
        this.repository = repository;
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
        repository.save(BroadcasterConfigurationEntity.fromDomain(source));
    }

    private void removeBroadcasterConfiguration(BroadcasterConfiguration source) {
        repository.deleteById(source.getId());
    }

    private void updateBroadcasterConfiguration(BroadcasterConfiguration source) {
        repository.findById(source.getId()).ifPresent(repository::save);
    }
}
