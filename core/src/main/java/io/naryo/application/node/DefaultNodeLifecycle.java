package io.naryo.application.node;

import java.io.IOException;
import java.util.*;

import io.reactivex.disposables.CompositeDisposable;

public final class DefaultNodeLifecycle implements NodeLifecycle {

    Map<UUID, CompositeDisposable> runningProcessesByNodeId;
    Map<UUID, NodeRunner> nodeRunnersByNodeId;

    @Override
    public void launch(Collection<NodeRunner> runners) {
        for (var runner : runners) {
            launch(runner);
        }
    }

    @Override
    public void launch(NodeRunner runner) {
        nodeRunnersByNodeId.put(runner.getNode().getId(), runner);
        runNode(runner);
    }

    @Override
    public void stop(UUID nodeId) {
        CompositeDisposable runningProcess = runningProcessesByNodeId.get(nodeId);

        if (runningProcess == null) {
            throw new IllegalArgumentException("Node with id " + nodeId + "is not running");
        }

        runningProcess.dispose();
        runningProcessesByNodeId.remove(nodeId);
    }

    @Override
    public void restart(UUID nodeId) {
        this.stop(nodeId);

        NodeRunner nodeRunner = nodeRunnersByNodeId.get(nodeId);

        if (nodeRunner == null) {
            throw new IllegalArgumentException("Node with id " + nodeId + "not found");
        }

        this.runNode(nodeRunner);
    }

    @Override
    public boolean isRunning(UUID nodeId) {
        return runningProcessesByNodeId.containsKey(nodeId);
    }

    @Override
    public int runningCount() {
        return runningProcessesByNodeId.size();
    }

    private void runNode(NodeRunner nodeRunner) {
        try {
            runningProcessesByNodeId.put(nodeRunner.getNode().getId(), nodeRunner.run());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
