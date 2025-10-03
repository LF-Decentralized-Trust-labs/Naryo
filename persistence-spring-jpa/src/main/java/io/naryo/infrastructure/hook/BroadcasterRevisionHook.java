package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.BroadcasterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterRevisionHook implements RevisionHook<Broadcaster> {

    private final BroadcasterEntityRepository repository;

    public BroadcasterRevisionHook(BroadcasterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<Broadcaster> diff) {
        for (Broadcaster add : diff.added()) {
            addBroadcaster(add);
        }
        for (Broadcaster remove : diff.removed()) {
            removeBroadcaster(remove);
        }
        for (DiffResult.Modified<Broadcaster> modified : diff.modified()) {
            updateBroadcaster(modified.after());
        }
    }

    @Override
    public void onAfterApply(Revision<Broadcaster> applied, DiffResult<Broadcaster> diff) {}

    private void addBroadcaster(Broadcaster broadcaster) {
        repository.save(BroadcasterEntity.fromDomain(broadcaster));
    }

    private void removeBroadcaster(Broadcaster broadcaster) {
        repository.deleteById(broadcaster.getId());
    }

    private void updateBroadcaster(Broadcaster broadcaster) {
        repository
                .findById(broadcaster.getId())
                .ifPresent(
                        filterDocument -> {
                            repository.save(BroadcasterEntity.fromDomain(broadcaster));
                        });
    }
}
