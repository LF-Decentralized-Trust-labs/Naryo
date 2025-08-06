package io.naryo.infrastructure.configuration.persistence.document.event.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreState;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "event_stores")
@Setter
public abstract class EventStoreConfigurationPropertiesDocument
        implements EventStoreConfigurationDescriptor {
    private @MongoId String nodeId;
    private @NotNull StoreState state;

    public EventStoreConfigurationPropertiesDocument(String nodeId, StoreState state) {
        this.nodeId = nodeId;
        this.state = state;
    }

    @Override
    public UUID getNodeId() {
        return UUID.fromString(this.nodeId);
    }

    @Override
    public StoreState getState() {
        return this.state;
    }
}
