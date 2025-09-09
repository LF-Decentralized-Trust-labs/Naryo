package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("filter_broadcaster_targets")
public class FilterBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements FilterBroadcasterTargetDescriptor {

    private @NotNull UUID filterId;

    public FilterBroadcasterTargetDocument(List<String> destinations, UUID filterId) {
        super(destinations);
        this.filterId = filterId;
    }
}
