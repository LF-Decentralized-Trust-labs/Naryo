package io.naryo.application.store.revision;

import java.util.Random;

import io.naryo.application.common.revision.BaseRevisionFingerprinterTest;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreConfigurationBuilder;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.ActiveStoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.HttpStoreConfigurationBuilder;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfigurationBuilder;
import org.junit.jupiter.api.BeforeEach;

class StoreRevisionFingerprinterTest extends BaseRevisionFingerprinterTest<StoreConfiguration> {

    protected StoreRevisionFingerprinterTest() {
        super(StoreConfiguration::getNodeId);
    }

    @BeforeEach
    void setUp() {
        var registry = new ActiveStoreConfigurationNormalizerRegistry();
        var activeNormalizer = new ActiveStoreConfigurationNormalizer(registry);
        var normalizer = new StoreConfigurationNormalizer(activeNormalizer);
        fingerprinter = new StoreRevisionFingerprinter(normalizer);
    }

    @Override
    protected StoreConfiguration createInput() {
        var random = new Random().nextInt(2);
        StoreConfigurationBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new InactiveStoreConfigurationBuilder();
                    case 1 -> new HttpStoreConfigurationBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
