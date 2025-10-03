package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.hook.RevisionHookBinder;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

@Component
public class JpaRevisionHookInitializer implements EnvironmentInitializer {

    private final RevisionHookBinder revisionHookBinder;
    private final NodeRevisionHook nodeRevisionHook;
    private final BroadcasterRevisionHook broadcasterRevisionHook;
    private final BroadcasterConfigurationRevisionHook broadcasterConfigurationRevisionHook;
    private final FilterRevisionHook filterRevisionHook;
    private final StoreRevisionHook storeRevisionHook;

    public JpaRevisionHookInitializer(
            RevisionHookBinder revisionHookBinder,
            NodeRevisionHook nodeRevisionHook,
            BroadcasterRevisionHook broadcasterRevisionHook,
            BroadcasterConfigurationRevisionHook broadcasterConfigurationRevisionHook,
            FilterRevisionHook filterRevisionHook,
            StoreRevisionHook storeRevisionHook) {
        this.revisionHookBinder = revisionHookBinder;
        this.nodeRevisionHook = nodeRevisionHook;
        this.broadcasterRevisionHook = broadcasterRevisionHook;
        this.broadcasterConfigurationRevisionHook = broadcasterConfigurationRevisionHook;
        this.filterRevisionHook = filterRevisionHook;
        this.storeRevisionHook = storeRevisionHook;
    }

    @Override
    public void initialize() {
        revisionHookBinder.register(Node.class, nodeRevisionHook);
        revisionHookBinder.register(Broadcaster.class, broadcasterRevisionHook);
        revisionHookBinder.register(
                BroadcasterConfiguration.class, broadcasterConfigurationRevisionHook);
        revisionHookBinder.register(Filter.class, filterRevisionHook);
        revisionHookBinder.register(StoreConfiguration.class, storeRevisionHook);
    }
}
