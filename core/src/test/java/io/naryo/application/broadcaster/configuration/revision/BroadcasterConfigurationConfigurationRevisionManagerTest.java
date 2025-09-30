package io.naryo.application.broadcaster.configuration.revision;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfigurationBuilder;

class BroadcasterConfigurationConfigurationRevisionManagerTest
        extends BaseConfigurationRevisionManagerTest<
                BroadcasterConfiguration,
                BroadcasterConfigurationConfigurationManager,
                BroadcasterConfigurationRevisionFingerprinter> {

    protected BroadcasterConfigurationConfigurationRevisionManagerTest() {
        super(BroadcasterConfiguration::getId, BroadcasterConfigurationConfigurationManager.class);
    }

    @Override
    protected ConfigurationRevisionManager<BroadcasterConfiguration> createManager(
            BroadcasterConfigurationConfigurationManager configurationManager,
            BroadcasterConfigurationRevisionFingerprinter fingerprinter,
            LiveRegistry<BroadcasterConfiguration> liveRegistry) {
        return new BroadcasterConfigurationConfigurationRevisionManager(
                configurationManager, fingerprinter, liveRegistry);
    }

    @Override
    protected BroadcasterConfigurationRevisionFingerprinter createFingerprinter() {
        return new BroadcasterConfigurationRevisionFingerprinter(
                new BroadcasterConfigurationNormalizer(
                        new BroadcasterConfigurationNormalizerRegistry()));
    }

    @Override
    protected BroadcasterConfiguration newItem() {
        return new HttpBroadcasterConfigurationBuilder().build();
    }

    @Override
    protected BroadcasterConfiguration updatedVariantOf(BroadcasterConfiguration base) {
        return new HttpBroadcasterConfigurationBuilder().withId(base.getId()).build();
    }
}
