package io.naryo.application.node;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public final class DefaultNodeLifecycle implements NodeLifecycle {

    Map<UUID, NodeRunner> nodeRunners;

    public DefaultNodeLifecycle() {
        this.nodeRunners = new HashMap<>();
    }

    @Override
    public void launch(Collection<NodeRunner> runners) {
        for (var runner : runners) {
            launch(runner);
        }
    }

    @Override
    public void launch(NodeRunner runner) {
        UUID nodeId = runner.getNode().getId();
        if (isRunning(nodeId)) {
            throw new IllegalArgumentException("Node with id " + nodeId + "is already running");
        }

        runNode(runner);
    }

    @Override
    public void stop(UUID nodeId) {
        if (!isRunning(nodeId)) {
            throw new IllegalArgumentException("Node with id " + nodeId + "is not running");
        }

        nodeRunners.get(nodeId).stop();
    }

    @Override
    public void restart(UUID nodeId, Supplier<NodeRunner> factory) {
        this.stop(nodeId);
        this.launch(factory.get());
    }

    @Override
    public boolean isRunning(UUID nodeId) {
        return nodeRunners.containsKey(nodeId) && nodeRunners.get(nodeId).isRunning();
    }

    @Override
    public int runningCount() {
        return Math.toIntExact(
                nodeRunners.entrySet().stream()
                        .filter(nrEntry -> nrEntry.getValue().isRunning())
                        .count());
    }

    @Override
    public NodeRunner getRunner(UUID nodeId) {
        return nodeRunners.get(nodeId);
    }

    private void runNode(NodeRunner nodeRunner) {
        try {
            nodeRunner.run();
            nodeRunners.put(nodeRunner.getNode().getId(), nodeRunner);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
