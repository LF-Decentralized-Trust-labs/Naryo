package io.naryo.application.store.revision;

import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.domain.configuration.store.StoreConfiguration;

public final class StoreConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<StoreConfiguration> {

    public StoreConfigurationRevisionManager(
            StoreConfigurationManager configurationManager,
            StoreRevisionFingerprinter fingerprinter,
            LiveRegistry<StoreConfiguration> live) {
        super(configurationManager, fingerprinter, StoreConfiguration::getNodeId, live);
    }
}
