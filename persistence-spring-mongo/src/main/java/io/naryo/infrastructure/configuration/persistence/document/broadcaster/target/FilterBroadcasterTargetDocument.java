package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "broadcasters")
@TypeAlias("filter_broadcaster_targets")
public class FilterBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements FilterBroadcasterTargetDescriptor {

    private @NotNull UUID filterId;

    public FilterBroadcasterTargetDocument(String destination, UUID filterId) {
        super(destination);
        this.filterId = filterId;
    }
}
