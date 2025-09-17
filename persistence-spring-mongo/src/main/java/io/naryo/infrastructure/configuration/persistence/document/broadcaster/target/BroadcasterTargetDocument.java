package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {

    private @Nullable @NotEmpty Set<String> destinations;

    public BroadcasterTargetDocument(Set<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public Set<String> getDestinations() {
        return destinations;
    }

    @Override
    public void setDestinations(Set<String> destinations) {
        this.destinations = destinations;
    }
}
