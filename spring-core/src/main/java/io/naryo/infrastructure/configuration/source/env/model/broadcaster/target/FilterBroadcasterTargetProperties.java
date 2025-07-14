package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class FilterBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements FilterBroadcasterTargetDescriptor {

    private @Getter @Setter @NotNull UUID filterId;

    protected FilterBroadcasterTargetProperties(String destination, UUID filterId) {
        super(BroadcasterTargetType.FILTER, destination);
        this.filterId = filterId;
    }

    public FilterBroadcasterTargetProperties() {
        super(BroadcasterTargetType.FILTER, null);
    }

    @Override
    public BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        FilterBroadcasterTargetProperties target =
                (FilterBroadcasterTargetProperties)
                        FilterBroadcasterTargetDescriptor.super.merge(other);

        if (other instanceof FilterBroadcasterTargetDescriptor descriptor) {
            if (!target.filterId.equals(descriptor.getFilterId())) {
                target.setFilterId(descriptor.getFilterId());
            }
        }

        return this;
    }
}
