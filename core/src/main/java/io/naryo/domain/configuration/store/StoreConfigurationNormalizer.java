package io.naryo.domain.configuration.store;

import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.ActiveStoreConfigurationNormalizer;
import io.naryo.domain.normalization.Normalizer;

public final class StoreConfigurationNormalizer implements Normalizer<StoreConfiguration> {

    private final ActiveStoreConfigurationNormalizer activeNormalizer;

    public StoreConfigurationNormalizer(ActiveStoreConfigurationNormalizer activeNormalizer) {
        this.activeNormalizer = activeNormalizer;
    }

    @Override
    public StoreConfiguration normalize(StoreConfiguration in) {
        return switch (in.getState()) {
            case ACTIVE -> activeNormalizer.normalize((ActiveStoreConfiguration) in);
            case INACTIVE -> in;
        };
    }
}
