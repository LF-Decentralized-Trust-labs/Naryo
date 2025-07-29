package io.naryo.infrastructure.configuration.persistence.document.event.store.block;

import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Document
public final class EventStoreTargetPropertiesDocument implements EventStoreTargetDescriptor {

    private @NotNull TargetType type;
    private @NotNull String destination;

    public EventStoreTargetPropertiesDocument(TargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }

    @Override
    public TargetType type() {
        return this.type;
    }

    @Override
    public String destination() {
        return this.destination;
    }
}
