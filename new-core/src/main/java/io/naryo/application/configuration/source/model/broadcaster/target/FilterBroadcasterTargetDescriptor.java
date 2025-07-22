package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.Optional;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTargetType;

public interface FilterBroadcasterTargetDescriptor extends BroadcasterTargetDescriptor {

    UUID getFilterId();

    void setFilterId(UUID filterId);

    @Override
    default BroadcasterTargetType getType() {
        return BroadcasterTargetType.FILTER;
    }
}
