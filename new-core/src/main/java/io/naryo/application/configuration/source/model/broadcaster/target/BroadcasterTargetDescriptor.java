package io.naryo.application.configuration.source.model.broadcaster.target;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

public interface BroadcasterTargetDescriptor
        extends MergeableDescriptor<BroadcasterTargetDescriptor> {

    BroadcasterTargetType getType();

    String getDestination();

    void setDestination(String destination);

    @Override
    default BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        if (other == null) {
            return this;
        }

        if (this.getType() != other.getType()) {
            return other;
        }

        if (!this.getDestination().equals(other.getDestination())) {
            this.setDestination(other.getDestination());
        }

        return this;
    }
}
