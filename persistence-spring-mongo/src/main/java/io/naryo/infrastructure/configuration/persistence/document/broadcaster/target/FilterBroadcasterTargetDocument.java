package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "broadcasters")
@TypeAlias("filter_broadcaster_targets")
public class FilterBroadcasterTargetDocument extends BroadcasterTargetDocument implements FilterBroadcasterTargetDescriptor {

    @NotNull
    private UUID filterId;


    public FilterBroadcasterTargetDocument(String destination, UUID filterId) {
        super(BroadcasterTargetType.FILTER, destination);
        this.filterId = filterId;
        }
    public FilterBroadcasterTargetDocument() {
        super(BroadcasterTargetType.FILTER, null);
    }

    @Override
    public UUID getFilterId() {
        return this.filterId;
    }

    @Override
    public void setFilterId(UUID filterId) {
        this.filterId = filterId;
    }
}
