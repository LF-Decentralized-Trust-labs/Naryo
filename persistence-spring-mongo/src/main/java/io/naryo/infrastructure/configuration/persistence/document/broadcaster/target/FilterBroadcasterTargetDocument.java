package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.FilterBroadcasterTargetProperties;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "broadcasters")
@TypeAlias("filter_broadcaster_targets")
public class FilterBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements FilterBroadcasterTargetDescriptor {

    @NotNull private UUID filterId;

    public FilterBroadcasterTargetDocument(String destination, UUID filterId) {
        super(destination);
        this.filterId = filterId;
    }

    @Override
    public UUID getFilterId() {
        return this.filterId;
    }

    @Override
    public void setFilterId(UUID filterId) {
        this.filterId = filterId;
    }

    @Override
    public BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        FilterBroadcasterTargetProperties target =
                (FilterBroadcasterTargetProperties)
                        FilterBroadcasterTargetDescriptor.super.merge(other);

        if (other instanceof FilterBroadcasterTargetDescriptor descriptor) {
            if (!target.getFilterId().equals(descriptor.getFilterId())) {
                target.setFilterId(descriptor.getFilterId());
            }
        }

        return this;
    }
}
