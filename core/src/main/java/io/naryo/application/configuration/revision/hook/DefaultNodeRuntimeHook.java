package io.naryo.application.configuration.revision.hook;

import java.util.Collection;
import java.util.List;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.domain.node.Node;

public record DefaultNodeRuntimeHook(NodeLifecycle nodeLifecycle, NodeInitializer nodeInitializer)
        implements RevisionHook<Node> {

    @Override
    public void onAfterApply(Revision<Node> applied, DiffResult<Node> diff) {
        if (!diff.added().isEmpty()) {
            addNodes(diff.added());
        }

        if (!diff.removed().isEmpty()) {
            removeNodes(diff.removed());
        }

        if (!diff.modified().isEmpty()) {
            updateNodes(diff.modified());
        }
    }

    private void addNodes(Collection<Node> nodes) {
        for (var node : nodes) {
            NodeRunner runner = nodeInitializer.initializeNode(node);
            nodeLifecycle.launch(runner);
        }
    }

    private void removeNodes(List<Node> nodes) {
        for (var node : nodes) {
            nodeLifecycle.stop(node.getId());
        }
    }

    private void updateNodes(List<DiffResult.Modified<Node>> nodeChanges) {
        for (var nodeData : nodeChanges) {
            NodeRunner runner = nodeInitializer.initializeNode(nodeData.after());
            nodeLifecycle.restart(nodeData.before().getId(), () -> runner);
        }
    }
}
