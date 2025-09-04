package io.naryo.infrastructure.configuration.persistence.document.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "event_stores")
@Setter
public abstract class StoreConfigurationPropertiesDocument implements StoreConfigurationDescriptor {
    private @MongoId String nodeId;

    public StoreConfigurationPropertiesDocument(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getNodeId() {
        return UUID.fromString(this.nodeId);
    }
}
