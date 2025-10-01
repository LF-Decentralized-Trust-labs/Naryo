package io.naryo.application.node;

import java.util.Collection;
import java.util.UUID;

public interface NodeLifecycle {
    void launch(Collection<NodeRunner> runners);

    void launch(NodeRunner runner);

    void stop(UUID nodeId);

    void restart(UUID nodeId);

    boolean isRunning(UUID nodeId);

    int runningCount();
}
