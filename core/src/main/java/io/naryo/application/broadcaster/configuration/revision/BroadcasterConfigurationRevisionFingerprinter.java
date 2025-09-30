package io.naryo.application.broadcaster.configuration.revision;

import io.naryo.application.configuration.revision.fingerprint.DefaultRevisionFingerprinter;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;

public final class BroadcasterConfigurationRevisionFingerprinter
        extends DefaultRevisionFingerprinter<BroadcasterConfiguration> {

    public BroadcasterConfigurationRevisionFingerprinter(
            BroadcasterConfigurationNormalizer normalizer) {
        super(normalizer);
    }
}
