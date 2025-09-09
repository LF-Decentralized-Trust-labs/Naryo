package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

import static io.naryo.application.common.util.MergeUtil.mergeCollections;

public interface BroadcasterTargetDescriptor
        extends MergeableDescriptor<BroadcasterTargetDescriptor> {

    BroadcasterTargetType getType();

    List<String> getDestinations();

    void setDestinations(List<String> destinations);

    @Override
    default BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        if (!this.getType().equals(other.getType())) {
            return this;
        }

        mergeCollections(this::setDestinations, this.getDestinations(), other.getDestinations());

        return this;
    }
}
