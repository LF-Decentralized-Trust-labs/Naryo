package io.naryo.application.broadcaster.revision;

import java.util.UUID;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterBuilder;

class BroadcasterConfigurationRevisionManagerTest
        extends BaseConfigurationRevisionManagerTest<
                Broadcaster, BroadcasterConfigurationManager, BroadcasterRevisionFingerprinter> {

    protected BroadcasterConfigurationRevisionManagerTest() {
        super(Broadcaster::getId, BroadcasterConfigurationManager.class);
    }

    @Override
    protected ConfigurationRevisionManager<Broadcaster> createManager(
            BroadcasterConfigurationManager configurationManager,
            BroadcasterRevisionFingerprinter fingerprinter,
            LiveRegistry<Broadcaster> liveRegistry) {
        return new BroadcasterConfigurationRevisionManager(
                configurationManager, fingerprinter, liveRegistry);
    }

    @Override
    protected BroadcasterRevisionFingerprinter createFingerprinter() {
        return new BroadcasterRevisionFingerprinter();
    }

    @Override
    protected Broadcaster newItem() {
        return new BroadcasterBuilder().build();
    }

    @Override
    protected Broadcaster updatedVariantOf(Broadcaster base) {
        return base.toBuilder().configurationId(UUID.randomUUID()).build();
    }
}
