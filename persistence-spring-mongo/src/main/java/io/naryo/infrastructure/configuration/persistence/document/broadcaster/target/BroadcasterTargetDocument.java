package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {

    private @Nullable @NotEmpty List<String> destinations;

    public BroadcasterTargetDocument(List<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public List<String> getDestinations() {
        return destinations;
    }

    @Override
    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
