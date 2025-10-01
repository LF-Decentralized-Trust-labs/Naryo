package io.naryo.application.store.revision;

import java.util.Random;

import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreConfigurationBuilder;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.ActiveStoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    private StoreConfigurationBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(2);
        return switch (random) {
            case 0 -> new InactiveStoreConfigurationBuilder();
            case 1 -> new HttpStoreConfigurationBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }

    private StoreConfiguration newHttpStoreConfiguration() {
        return new HttpStoreConfigurationBuilder().build();
    }

    /**
     * This test is overridden because adding the same InactiveStoreConfiguration twice does not
     * throw. This is due to the fact that the InactiveStoreConfiguration does not have any
     * attributes.
     */
    @Override
    @Test
    protected void apply_addWithSameIdButDifferentContent_throwRevisionConflict() throws Exception {
        manager.initialize();
        StoreConfiguration base = newHttpStoreConfiguration();
        manager.apply(new AddOperation<>(base));

        StoreConfiguration other = updatedVariantOf(base);

        assertThrows(
                RevisionConflictException.class, () -> manager.apply(new AddOperation<>(other)));

        verify(liveRegistry, times(2)).refresh(any());

        verify(hookSpy, times(1)).onBeforeApply(any());
        verify(hookSpy, times(1)).onAfterApply(any(), any());
    }
}
