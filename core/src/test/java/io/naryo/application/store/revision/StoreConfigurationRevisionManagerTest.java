package io.naryo.application.store.revision;

import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreConfigurationBuilder;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.ActiveStoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;

class StoreConfigurationRevisionManagerTest
        extends BaseConfigurationRevisionManagerTest<
                StoreConfiguration, StoreConfigurationManager, StoreRevisionFingerprinter> {

    protected StoreConfigurationRevisionManagerTest() {
        super(StoreConfiguration::getNodeId, StoreConfigurationManager.class);
    }

    @Override
    protected ConfigurationRevisionManager<StoreConfiguration> createManager(
            StoreConfigurationManager configurationManager,
            StoreRevisionFingerprinter fingerprinter,
            LiveRegistry<StoreConfiguration> liveRegistry) {
        return new StoreConfigurationRevisionManager(
                configurationManager, fingerprinter, liveRegistry);
    }

    @Override
    protected StoreRevisionFingerprinter createFingerprinter() {
        return new StoreRevisionFingerprinter(
                new StoreConfigurationNormalizer(
                        new ActiveStoreConfigurationNormalizer(
                                new ActiveStoreConfigurationNormalizerRegistry())));
    }

    @Override
    protected StoreConfiguration newItem() {
        return newBuilder().build();
    }

    @Override
    protected StoreConfiguration updatedVariantOf(StoreConfiguration base) {
        return newBuilder().withNodeId(base.getNodeId()).build();
    }

    /**
     * This method is used to create a new StoreConfigurationBuilder. Only returns a
     * HttpStoreConfigurationBuilder because the InactiveStoreConfigurationBuilder does not have any
     * attributes. This causes some tests to fail.
     */
    private StoreConfigurationBuilder<?, ?> newBuilder() {
        return new HttpStoreConfigurationBuilder();
    }

    private StoreConfiguration newHttpStoreConfiguration() {
        return new HttpStoreConfigurationBuilder().build();
    }
}
