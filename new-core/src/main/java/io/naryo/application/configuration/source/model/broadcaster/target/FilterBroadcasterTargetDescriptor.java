package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.UUID;

public interface FilterBroadcasterTargetDescriptor extends BroadcasterTargetDescriptor {

    UUID getFilterId();

    void setFilterId(UUID filterId);
}
