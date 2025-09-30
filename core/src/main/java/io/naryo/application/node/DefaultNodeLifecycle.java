package io.naryo.application.node;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import io.reactivex.disposables.CompositeDisposable;

public final class DefaultNodeLifecycle implements NodeLifecycle {

    Map<UUID, CompositeDisposable> runningNodes;

    @Override
    public void launch(Collection<NodeRunner> runners) {
        for (var runner : runners) {
            runNode(runner);
        }
    }

    @Override
    public void launch(NodeRunner runner) {
        runNode(runner);
    }

    @Override
    public void stopAndRemove(UUID nodeId) {
        CompositeDisposable runningNodeDisposable = runningNodes.get(nodeId);
        if (runningNodeDisposable != null) {
            runningNodeDisposable.dispose();
            runningNodes.remove(nodeId);
        }
    }

    @Override
    public void restart(UUID nodeId, Supplier<NodeRunner> factory) {
        this.stopAndRemove(nodeId);
        this.runNode(factory.get());
    }

    @Override
    public boolean isRunning(UUID nodeId) {
        return runningNodes.containsKey(nodeId);
    }

    @Override
    public int runningCount() {
        return runningNodes.size();
    }

    private void runNode(NodeRunner nodeRunner) {
        try {
            runningNodes.put(nodeRunner.getNode().getId(), nodeRunner.run());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
