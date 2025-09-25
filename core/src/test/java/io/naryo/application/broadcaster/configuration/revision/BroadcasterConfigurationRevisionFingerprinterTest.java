package io.naryo.application.broadcaster.configuration.revision;

import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.common.revision.BaseRevisionFingerprinterTest;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfigurationBuilder;
import org.junit.jupiter.api.BeforeEach;

class BroadcasterConfigurationRevisionFingerprinterTest
        extends BaseRevisionFingerprinterTest<BroadcasterConfiguration> {

    protected BroadcasterConfigurationRevisionFingerprinterTest() {
        super(BroadcasterConfiguration::getId);
    }

    @BeforeEach
    void setUp() {
        var registry = new BroadcasterConfigurationNormalizerRegistry();
        var normalizer = new BroadcasterConfigurationNormalizer(registry);
        fingerprinter = new BroadcasterConfigurationRevisionFingerprinter(normalizer);
    }

    @Override
    protected BroadcasterConfiguration createInput() {
        return new HttpBroadcasterConfigurationBuilder().build();
    }
}
