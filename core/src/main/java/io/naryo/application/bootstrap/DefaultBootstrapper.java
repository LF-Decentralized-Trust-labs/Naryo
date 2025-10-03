package io.naryo.application.bootstrap;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.configuration.revision.hook.RevisionHookBinder;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManagers;
import io.naryo.application.configuration.revision.worker.RevisionOperationWorkers;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;

public final class DefaultBootstrapper implements Bootstrapper {

    private final ConfigurationRevisionManagers managers;
    private final NodeInitializer nodeInitializer;
    private final NodeLifecycle nodeLifecycle;
    private final RevisionOperationWorkers workers;
    private final RevisionHookBinder hookBinder;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public DefaultBootstrapper(
            ConfigurationRevisionManagers managers,
            NodeInitializer nodeInitializer,
            NodeLifecycle nodeLifecycle,
            RevisionOperationWorkers workers,
            RevisionHookBinder hookBinder) {
        this.managers = Objects.requireNonNull(managers, "managers cannot be null");
        this.nodeInitializer =
                Objects.requireNonNull(nodeInitializer, "nodeInitializer cannot be null");
        this.nodeLifecycle = Objects.requireNonNull(nodeLifecycle, "nodeLifecycle cannot be null");
        this.workers = Objects.requireNonNull(workers, "workers cannot be null");
        this.hookBinder = Objects.requireNonNull(hookBinder, "hookBinder cannot be null");
    }

    @Override
    public void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }

        managers.initialize();

        hookBinder.bindDefaults();

        workers.initialize();

        Collection<NodeRunner> nodeRunners = nodeInitializer.initialize();

        nodeLifecycle.launch(nodeRunners);
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }
}
