package io.naryo.application.node;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

public interface NodeLifecycle {
    void launch(Collection<NodeRunner> runners);

    void launch(NodeRunner runner);

    void stopAndRemove(UUID nodeId);

    void restart(UUID nodeId, Supplier<NodeRunner> factory);

    boolean isRunning(UUID nodeId);

    int runningCount();
}
