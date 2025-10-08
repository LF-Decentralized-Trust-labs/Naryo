package io.naryo.application.configuration.revision.hook;

import java.util.concurrent.atomic.AtomicBoolean;

import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public final class DefaultRevisionHookBinder implements RevisionHookBinder {

    private final NodeConfigurationRevisionManager nodeConfigurationRevisionManager;
    private final BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager;
    private final BroadcasterConfigurationConfigurationRevisionManager
            broadcasterConfigurationConfigurationRevisionManager;
    private final StoreConfigurationRevisionManager storeConfigurationRevisionManager;
    private final FilterConfigurationRevisionManager filterConfigurationRevisionManager;
    private final AtomicBoolean defaultsBound = new AtomicBoolean(false);
    private final DefaultFilterRuntimeHook defaultFilterRuntimeHook;
    private final DefaultNodeRuntimeHook defaultNodeRuntimeHook;
    private final DefaultStoreConfigurationRuntimeHook defaultStoreConfigurationRuntimeHook;

    public DefaultRevisionHookBinder(
            NodeConfigurationRevisionManager nodeConfigurationRevisionManager,
            BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager,
            BroadcasterConfigurationConfigurationRevisionManager
                    broadcasterConfigurationConfigurationRevisionManager,
            StoreConfigurationRevisionManager storeConfigurationRevisionManager,
            FilterConfigurationRevisionManager filterConfigurationRevisionManager,
            DefaultFilterRuntimeHook defaultFilterRuntimeHook,
            DefaultNodeRuntimeHook defaultNodeRuntimeHook,
            DefaultStoreConfigurationRuntimeHook defaultStoreConfigurationRuntimeHook) {
        this.nodeConfigurationRevisionManager = nodeConfigurationRevisionManager;
        this.broadcasterConfigurationRevisionManager = broadcasterConfigurationRevisionManager;
        this.broadcasterConfigurationConfigurationRevisionManager =
                broadcasterConfigurationConfigurationRevisionManager;
        this.storeConfigurationRevisionManager = storeConfigurationRevisionManager;
        this.filterConfigurationRevisionManager = filterConfigurationRevisionManager;
        this.defaultFilterRuntimeHook = defaultFilterRuntimeHook;
        this.defaultNodeRuntimeHook = defaultNodeRuntimeHook;
        this.defaultStoreConfigurationRuntimeHook = defaultStoreConfigurationRuntimeHook;
    }

    @Override
    public void bindDefaults() {
        if (defaultsBound.get()) {
            return;
        }

        filterConfigurationRevisionManager.registerHook(defaultFilterRuntimeHook);
        nodeConfigurationRevisionManager.registerHook(defaultNodeRuntimeHook);
        storeConfigurationRevisionManager.registerHook(defaultStoreConfigurationRuntimeHook);

        defaultsBound.set(true);
    }

    @Override
    public <T> void register(Class<T> domainClass, RevisionHook<T> hook) {
        if (Node.class.isAssignableFrom(domainClass)) {
            nodeConfigurationRevisionManager.registerHook((RevisionHook<Node>) hook);
        } else if (Broadcaster.class.isAssignableFrom(domainClass)) {
            broadcasterConfigurationRevisionManager.registerHook((RevisionHook<Broadcaster>) hook);
        } else if (BroadcasterConfiguration.class.isAssignableFrom(domainClass)) {
            broadcasterConfigurationConfigurationRevisionManager.registerHook(
                    (RevisionHook<BroadcasterConfiguration>) hook);
        } else if (StoreConfiguration.class.isAssignableFrom(domainClass)) {
            storeConfigurationRevisionManager.registerHook((RevisionHook<StoreConfiguration>) hook);
        } else if (Filter.class.isAssignableFrom(domainClass)) {
            filterConfigurationRevisionManager.registerHook((RevisionHook<Filter>) hook);
        } else {
            throw new IllegalArgumentException("Unsupported domain class: " + domainClass);
        }
    }
}
