package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.Optional;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BroadcasterTargetDescriptor
        extends MergeableDescriptor<BroadcasterTargetDescriptor> {

    BroadcasterTargetType getType();

    Optional<String> getDestination();

    void setDestination(String destination);

    @Override
    default BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        if (!this.getType().equals(other.getType())) {
            return this;
        }

        mergeOptionals(this::setDestination, this.getDestination(), other.getDestination());

        return this;
    }
}
