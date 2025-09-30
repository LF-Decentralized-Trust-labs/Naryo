package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.BroadcasterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterRevisionHook implements RevisionHook<BroadcasterDescriptor> {

    private final BroadcasterEntityRepository repository;

    public BroadcasterRevisionHook(BroadcasterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<BroadcasterDescriptor> diff) {
        for (BroadcasterDescriptor add : diff.added()) {
            addBroadcaster(add);
        }
        for (BroadcasterDescriptor remove : diff.removed()) {
            removeBroadcaster(remove);
        }
        for (DiffResult.Modified<BroadcasterDescriptor> modified : diff.modified()) {
            updateBroadcaster(modified.after());
        }
    }

    @Override
    public void onAfterApply(
            Revision<BroadcasterDescriptor> applied, DiffResult<BroadcasterDescriptor> diff) {
        // Run post-publication actions (e.g., selective filter sync; broadcaster
        // add/remove/restart)
    }

    private void addBroadcaster(BroadcasterDescriptor broadcaster) {
        repository.save(BroadcasterEntity.fromDescriptor(broadcaster));
    }

    private void removeBroadcaster(BroadcasterDescriptor broadcaster) {
        repository.deleteById(broadcaster.getId());
    }

    private void updateBroadcaster(BroadcasterDescriptor broadcaster) {
        repository.findById(broadcaster.getId()).ifPresent(repository::save);
    }
}
