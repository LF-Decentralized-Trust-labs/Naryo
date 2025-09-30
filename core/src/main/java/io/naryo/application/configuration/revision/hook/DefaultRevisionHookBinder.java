package io.naryo.application.configuration.revision.hook;

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

public class DefaultRevisionHookBinder implements RevisionHookBinder {

    private final NodeConfigurationRevisionManager nodeConfigurationRevisionManager;
    private final BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager;
    private final BroadcasterConfigurationConfigurationRevisionManager
            broadcasterConfigurationConfigurationRevisionManager;
    private final StoreConfigurationRevisionManager storeConfigurationRevisionManager;
    private final FilterConfigurationRevisionManager filterConfigurationRevisionManager;

    public DefaultRevisionHookBinder(
            NodeConfigurationRevisionManager nodeConfigurationRevisionManager,
            BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager,
            BroadcasterConfigurationConfigurationRevisionManager
                    broadcasterConfigurationConfigurationRevisionManager,
            StoreConfigurationRevisionManager storeConfigurationRevisionManager,
            FilterConfigurationRevisionManager filterConfigurationRevisionManager) {
        this.nodeConfigurationRevisionManager = nodeConfigurationRevisionManager;
        this.broadcasterConfigurationRevisionManager = broadcasterConfigurationRevisionManager;
        this.broadcasterConfigurationConfigurationRevisionManager =
                broadcasterConfigurationConfigurationRevisionManager;
        this.storeConfigurationRevisionManager = storeConfigurationRevisionManager;
        this.filterConfigurationRevisionManager = filterConfigurationRevisionManager;
    }

    @Override
    public void bindDefaults() {
        // Todo: Bind default revision hooks
    }

    @Override
    public <T> void register(Class<T> domainClass, RevisionHook<T> hook) {
        if (domainClass == Node.class) {
            nodeConfigurationRevisionManager.registerHook((RevisionHook<Node>) hook);
        } else if (domainClass == Broadcaster.class) {
            broadcasterConfigurationRevisionManager.registerHook((RevisionHook<Broadcaster>) hook);
        } else if (domainClass == BroadcasterConfiguration.class) {
            broadcasterConfigurationConfigurationRevisionManager.registerHook(
                    (RevisionHook<BroadcasterConfiguration>) hook);
        } else if (domainClass == StoreConfiguration.class) {
            storeConfigurationRevisionManager.registerHook((RevisionHook<StoreConfiguration>) hook);
        } else if (domainClass == Filter.class) {
            filterConfigurationRevisionManager.registerHook((RevisionHook<Filter>) hook);
        } else {
            throw new IllegalArgumentException("Unsupported domain class: " + domainClass);
        }
    }
}
