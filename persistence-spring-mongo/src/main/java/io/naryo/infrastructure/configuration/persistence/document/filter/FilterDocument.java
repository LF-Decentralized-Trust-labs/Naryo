package io.naryo.infrastructure.configuration.persistence.document.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "filters")
@Setter
public abstract class FilterDocument implements FilterDescriptor {

    @MongoId private final String id;

    @Nullable private String name;

    @Nullable private String nodeId;

    public FilterDocument(String id, String name, String nodeId) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
    }

    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<UUID> getNodeId() {
        return nodeId == null ? Optional.empty() : Optional.of(UUID.fromString(nodeId));
    }

    @Override
    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId.toString();
    }
}
