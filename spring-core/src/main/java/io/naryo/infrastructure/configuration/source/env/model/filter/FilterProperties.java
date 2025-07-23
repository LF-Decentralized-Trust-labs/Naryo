package io.naryo.infrastructure.configuration.source.env.model.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
public abstract class FilterProperties implements FilterDescriptor {

    private final @Getter @NotNull UUID id;
    private @Nullable String name;
    private @Nullable UUID nodeId;

    public FilterProperties(UUID id, String name, UUID nodeId) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<UUID> getNodeId() {
        return Optional.ofNullable(nodeId);
    }
}
