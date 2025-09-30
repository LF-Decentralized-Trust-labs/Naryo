package io.naryo.application.broadcaster.revision;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.domain.broadcaster.Broadcaster;

public final class BroadcasterConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<Broadcaster> {

    public BroadcasterConfigurationRevisionManager(
            BroadcasterConfigurationManager configurationManager,
            BroadcasterRevisionFingerprinter fingerprinter,
            LiveRegistry<Broadcaster> live) {
        super(configurationManager, fingerprinter, Broadcaster::getId, live);
    }
}
