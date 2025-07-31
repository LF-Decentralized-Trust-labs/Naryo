package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface BroadcasterCacheConfigurationDescriptor
        extends MergeableDescriptor<BroadcasterCacheConfigurationDescriptor> {

    Duration getExpirationTime();

    @Override
    default BroadcasterCacheConfigurationDescriptor merge(
            BroadcasterCacheConfigurationDescriptor other) {
        return this;
    }
}
