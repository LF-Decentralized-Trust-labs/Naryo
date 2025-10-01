package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.target.BroadcasterDocument;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterDocumentRevisionHook implements RevisionHook<Broadcaster> {
    private final BroadcasterDocumentRepository repository;

    public BroadcasterDocumentRevisionHook(BroadcasterDocumentRepository repository) {
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
        repository.save(BroadcasterDocument.fromDomain(broadcaster));
    }

    private void removeBroadcaster(Broadcaster broadcaster) {
        repository.deleteById(broadcaster.getId().toString());
    }

    private void updateBroadcaster(Broadcaster broadcaster) {
        repository.findById(broadcaster.getId().toString()).ifPresent(repository::save);
    }
}
