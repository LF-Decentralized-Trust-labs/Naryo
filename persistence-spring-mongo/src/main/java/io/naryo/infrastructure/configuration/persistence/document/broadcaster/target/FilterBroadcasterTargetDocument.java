package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static io.naryo.application.common.util.MergeUtil.mergeValues;

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
        FilterBroadcasterTargetDescriptor.super.merge(other);

        if (other instanceof FilterBroadcasterTargetDescriptor descriptor) {
            if (!this.getFilterId().equals(descriptor.getFilterId())) {
                this.setFilterId(descriptor.getFilterId());
            }
            mergeValues(this::setFilterId, this.getFilterId(), descriptor.getFilterId());
        }

        return this;
    }
}
