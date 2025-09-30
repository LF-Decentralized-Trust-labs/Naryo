package io.naryo.application.bootstrap;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManagers;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;

public final class DefaultBootstrapper implements Bootstrapper {

    private final ConfigurationRevisionManagers managers;
    private final NodeInitializer nodeInitializer;
    private final NodeLifecycle nodeLifecycle;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public DefaultBootstrapper(
            ConfigurationRevisionManagers managers,
            NodeInitializer nodeInitializer,
            NodeLifecycle nodeLifecycle) {
        this.managers = Objects.requireNonNull(managers, "managers cannot be null");
        this.nodeInitializer =
                Objects.requireNonNull(nodeInitializer, "nodeInitializer cannot be null");
        this.nodeLifecycle = Objects.requireNonNull(nodeLifecycle, "nodeLifecycle cannot be null");
    }

    @Override
    public void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }

        managers.initialize();

        // TODO: Bind default hooks via an injected hook binder to support runtime config changes
        // hookBinder.bindDefaults();

        Collection<NodeRunner> nodeRunners = nodeInitializer.initialize();

        nodeLifecycle.launch(nodeRunners);
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }
}
