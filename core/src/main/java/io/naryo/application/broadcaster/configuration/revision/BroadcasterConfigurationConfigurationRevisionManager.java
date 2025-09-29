package io.naryo.application.broadcaster.configuration.revision;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;

public final class BroadcasterConfigurationConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<BroadcasterConfiguration> {

    public BroadcasterConfigurationConfigurationRevisionManager(
            BroadcasterConfigurationConfigurationManager configurationManager,
            BroadcasterConfigurationRevisionFingerprinter fingerprinter,
            LiveRegistry<BroadcasterConfiguration> live) {
        super(configurationManager, fingerprinter, BroadcasterConfiguration::getId, live);
    }
}
