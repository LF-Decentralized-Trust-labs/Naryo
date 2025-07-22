package io.naryo.application.configuration.source.model.broadcaster.target;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BroadcasterTargetDescriptor
        extends MergeableDescriptor<BroadcasterTargetDescriptor> {

    BroadcasterTargetType getType();

    Optional<String> getDestination();

    void setDestination(String destination);

    @Override
    default BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(
                this::setDestination,
                this.getDestination(),
                other.getDestination());

        return this;
    }
}
