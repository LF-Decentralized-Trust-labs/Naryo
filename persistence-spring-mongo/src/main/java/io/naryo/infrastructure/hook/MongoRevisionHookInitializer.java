package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.hook.RevisionHookBinder;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.beans.revision.HookInitializer;
import org.springframework.stereotype.Component;

@Component
public class MongoRevisionHookInitializer implements HookInitializer {

    private final RevisionHookBinder revisionHookBinder;
    private final NodeDocumentRevisionHook nodeDocumentRevisionHook;
    private final BroadcasterDocumentRevisionHook broadcasterDocumentRevisionHook;
    private final BroadcasterConfigurationDocumentRevisionHook
            broadcasterConfigurationDocumentRevisionHook;
    private final FilterDocumentRevisionHook filterDocumentRevisionHook;
    private final StoreConfigurationPropertiesDocumentRevisionHook storeDocumentRevisionHook;

    public MongoRevisionHookInitializer(
            RevisionHookBinder revisionHookBinder,
            NodeDocumentRevisionHook nodeDocumentRevisionHook,
            BroadcasterDocumentRevisionHook broadcasterDocumentRevisionHook,
            BroadcasterConfigurationDocumentRevisionHook
                    broadcasterConfigurationDocumentRevisionHook,
            FilterDocumentRevisionHook filterDocumentRevisionHook,
            StoreConfigurationPropertiesDocumentRevisionHook storeDocumentRevisionHook) {
        this.revisionHookBinder = revisionHookBinder;
        this.nodeDocumentRevisionHook = nodeDocumentRevisionHook;
        this.broadcasterDocumentRevisionHook = broadcasterDocumentRevisionHook;
        this.broadcasterConfigurationDocumentRevisionHook =
                broadcasterConfigurationDocumentRevisionHook;
        this.filterDocumentRevisionHook = filterDocumentRevisionHook;
        this.storeDocumentRevisionHook = storeDocumentRevisionHook;
    }

    @Override
    public void initialize() {
        revisionHookBinder.register(Node.class, nodeDocumentRevisionHook);
        revisionHookBinder.register(Broadcaster.class, broadcasterDocumentRevisionHook);
        revisionHookBinder.register(
                BroadcasterConfiguration.class, broadcasterConfigurationDocumentRevisionHook);
        revisionHookBinder.register(Filter.class, filterDocumentRevisionHook);
        revisionHookBinder.register(StoreConfiguration.class, storeDocumentRevisionHook);
    }
}
