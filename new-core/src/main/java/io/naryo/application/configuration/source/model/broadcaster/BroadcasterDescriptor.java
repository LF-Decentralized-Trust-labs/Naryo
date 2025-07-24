package io.naryo.application.configuration.source.model.broadcaster;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;

import static io.naryo.application.common.util.MergeUtil.mergeDescriptors;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BroadcasterDescriptor extends MergeableDescriptor<BroadcasterDescriptor> {

    UUID getId();

    Optional<UUID> getConfigurationId();

    <T extends BroadcasterTargetDescriptor> Optional<T> getTarget();

    void setConfigurationId(UUID configurationId);

    void setTarget(BroadcasterTargetDescriptor target);

    @Override
    default BroadcasterDescriptor merge(BroadcasterDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(
                this::setConfigurationId, this.getConfigurationId(), other.getConfigurationId());
        mergeDescriptors(this::setTarget, this.getTarget(), other.getTarget());

        return this;
    }
}
