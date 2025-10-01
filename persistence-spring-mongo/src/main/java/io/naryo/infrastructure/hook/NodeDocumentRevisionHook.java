package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class NodeDocumentRevisionHook implements RevisionHook<Node> {

    private final NodePropertiesDocumentRepository repository;

    public NodeDocumentRevisionHook(NodePropertiesDocumentRepository repository) {
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

    private void addNode(Node node) {
        repository.save(NodePropertiesDocument.fromDomain(node));
    }

    private void removeNode(Node node) {
        repository.deleteById(node.getId().toString());
    }

    private void updateNode(Node node) {
        repository
                .findById(node.getId().toString())
                .ifPresent(
                        filterDocument -> {
                            repository.save(NodePropertiesDocument.fromDomain(node));
                        });
    }
}
