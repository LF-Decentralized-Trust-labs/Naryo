package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface BroadcasterCacheConfigurationDescriptor
        extends MergeableDescriptor<BroadcasterCacheConfigurationDescriptor> {

    Duration getExpirationTime();

    void setExpirationTime(Duration expirationTime);

    @Override
    default BroadcasterCacheConfigurationDescriptor merge(
            BroadcasterCacheConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getExpirationTime().equals(other.getExpirationTime())) {
            this.setExpirationTime(other.getExpirationTime());
        }

        return this;
    }
}
