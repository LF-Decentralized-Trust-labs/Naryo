package io.naryo.infrastructure.configuration.persistence.document.filter;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.filter.FilterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "filters")
@Getter
public abstract class FilterDocument implements FilterDescriptor {

    @MongoId
    private String id;

    @NotNull
    private String name;

    @NotNull
    private FilterType type;

    @NotNull
    private String nodeId;

    public FilterDocument(String id, String name, FilterType type, String nodeId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.nodeId = nodeId;
    }

    @Override
    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public UUID getNodeId() {
        return UUID.fromString(this.nodeId);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId.toString();
    }

}
