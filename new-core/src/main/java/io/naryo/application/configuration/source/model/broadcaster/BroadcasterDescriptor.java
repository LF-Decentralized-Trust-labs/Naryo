package io.naryo.application.configuration.source.model.broadcaster;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;

public interface BroadcasterDescriptor extends MergeableDescriptor<BroadcasterDescriptor> {

    UUID id();

    UUID configurationId();

    BroadcasterTargetDescriptor target();

    void setConfigurationId(UUID configurationId);

    void setTarget(BroadcasterTargetDescriptor target);

    @Override
    default BroadcasterDescriptor merge(BroadcasterDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.configurationId().equals(other.configurationId())) {
            this.setConfigurationId(other.configurationId());
        }

        if (!this.target().equals(other.target())) {
            this.setTarget(this.target().merge(other.target()));
        }

        return this;
    }
}
