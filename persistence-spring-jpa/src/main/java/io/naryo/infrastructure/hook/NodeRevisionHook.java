package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodeEntityRepository;

public class NodeRevisionHook implements RevisionHook<NodeDescriptor> {

    private final NodeEntityRepository repository;

    public NodeRevisionHook(NodeEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<NodeDescriptor> diff) {
        // Todo: manage each diffResult array: add/remove/update nodes
        for (NodeDescriptor add : diff.added()) {
            addNode(add);
        }
        for (NodeDescriptor remove : diff.removed()) {
            removeNode(remove);
        }
        for (DiffResult.Modified<NodeDescriptor> modified : diff.modified()) {
            updateNode(modified.after());
        }
    }

    @Override
    public void onAfterApply(Revision<NodeDescriptor> applied, DiffResult<NodeDescriptor> diff) {
        // Run post-publication actions (e.g., selective filter sync; node add/remove/restart)
    }

    private void addNode(NodeDescriptor descriptor) {
        repository.save(NodeEntity.fromDescriptor(descriptor));
    }

    private void removeNode(NodeDescriptor descriptor) {
        repository.deleteById(descriptor.getId());
    }

    private void updateNode(NodeDescriptor descriptor) {
        repository.findById(descriptor.getId()).ifPresent(repository::save);
    }
}
