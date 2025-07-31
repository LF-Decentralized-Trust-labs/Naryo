package io.naryo.domain.broadcaster.target;

import java.util.UUID;

public class FilterEventBroadcasterTargetBuilder
        extends BroadcasterTargetBuilder<
                FilterEventBroadcasterTargetBuilder, FilterEventBroadcasterTarget> {

    private UUID filterId;

    @Override
    public FilterEventBroadcasterTargetBuilder self() {
        return this;
    }

    @Override
    public FilterEventBroadcasterTarget build() {
        return new FilterEventBroadcasterTarget(this.getDestination(), this.getFilterId());
    }

    public FilterEventBroadcasterTargetBuilder withFilterId(UUID filterId) {
        this.filterId = filterId;
        return this;
    }

    protected UUID getFilterId() {
        return this.filterId == null ? UUID.randomUUID() : this.filterId;
    }
}
