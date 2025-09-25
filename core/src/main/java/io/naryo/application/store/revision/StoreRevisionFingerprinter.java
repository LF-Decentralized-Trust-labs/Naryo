package io.naryo.application.store.revision;

import io.naryo.application.common.revision.DefaultRevisionFingerprinter;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;

public final class StoreRevisionFingerprinter
        extends DefaultRevisionFingerprinter<StoreConfiguration> {

    public StoreRevisionFingerprinter(StoreConfigurationNormalizer normalizer) {
        super(normalizer);
    }
}
