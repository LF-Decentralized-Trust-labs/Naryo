package io.naryo.application.configuration.revision.hook;

import java.util.*;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.domain.configuration.store.StoreConfiguration;

public record DefaultStoreConfigurationRuntimeHook(
        NodeLifecycle nodeLifecycle,
        NodeInitializer nodeInitializer,
        NodeConfigurationRevisionManager nodeConfigurationRevisionManager)
        implements RevisionHook<StoreConfiguration> {

    @Override
    public void onAfterApply(
            Revision<StoreConfiguration> applied, DiffResult<StoreConfiguration> diff) {
        if (!diff.removed().isEmpty()) {
            Collection<UUID> nodeIdsToRemove =
                    diff.removed().stream().map(StoreConfiguration::getNodeId).toList();
            stopNodes(nodeIdsToRemove);
        }

        if (!diff.modified().isEmpty()) {
            Collection<UUID> affectedNodeIds =
                    diff.modified().stream().map(modified -> modified.after().getNodeId()).toList();
            restartNodes(affectedNodeIds);
        }
    }

    private void restartNodes(Collection<UUID> nodeIds) {
        for (var nodeId : nodeIds) {
            nodeConfigurationRevisionManager.liveRegistry().active().domainItems().stream()
                    .filter(node -> node.getId().equals(nodeId))
                    .findFirst()
                    .ifPresent(
                            node -> {
                                NodeRunner runner = nodeInitializer.initializeNode(node);
                                nodeLifecycle.restart(nodeId, () -> runner);
                            });
        }
    }

    private void stopNodes(Collection<UUID> nodeIds) {
        for (var nodeId : nodeIds) {
            nodeLifecycle.stop(nodeId);
        }
    }
}
