package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodeEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class NodeRevisionHook implements RevisionHook<Node> {

    private final NodeEntityRepository repository;

    public NodeRevisionHook(NodeEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<Node> diff) {
        for (Node add : diff.added()) {
            addNode(add);
        }
        for (Node remove : diff.removed()) {
            removeNode(remove);
        }
        for (DiffResult.Modified<Node> modified : diff.modified()) {
            updateNode(modified.after());
        }
    }

    @Override
    public void onAfterApply(Revision<Node> applied, DiffResult<Node> diff) {}

    private void addNode(Node source) {
        repository.save(NodeEntity.fromDomain(source));
    }

    private void removeNode(Node source) {
        repository.deleteById(source.getId());
    }

    private void updateNode(Node source) {
        repository.findById(source.getId()).ifPresent(repository::save);
    }
}
