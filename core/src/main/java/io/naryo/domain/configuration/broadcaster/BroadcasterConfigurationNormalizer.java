package io.naryo.domain.configuration.broadcaster;

import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.domain.normalization.Normalizer;

public final class BroadcasterConfigurationNormalizer
        implements Normalizer<BroadcasterConfiguration> {

    private final BroadcasterConfigurationNormalizerRegistry registry;

    public BroadcasterConfigurationNormalizer(BroadcasterConfigurationNormalizerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public BroadcasterConfiguration normalize(BroadcasterConfiguration in) {
        if (in == null) {
            return null;
        }

        return registry.normalize(in.getType().getName(), in);
    }
}
