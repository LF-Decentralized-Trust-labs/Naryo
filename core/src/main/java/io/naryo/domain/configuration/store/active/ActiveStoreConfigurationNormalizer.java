package io.naryo.domain.configuration.store.active;

import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.normalization.Normalizer;

public final class ActiveStoreConfigurationNormalizer
        implements Normalizer<ActiveStoreConfiguration> {

    private final ActiveStoreConfigurationNormalizerRegistry registry;

    public ActiveStoreConfigurationNormalizer(ActiveStoreConfigurationNormalizerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public ActiveStoreConfiguration normalize(ActiveStoreConfiguration in) {
        if (in == null) {
            return null;
        }
        return registry.normalize(in.getType().getName(), in);
    }
}
