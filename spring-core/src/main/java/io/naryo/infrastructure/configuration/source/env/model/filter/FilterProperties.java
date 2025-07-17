package io.naryo.infrastructure.configuration.source.env.model.filter;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.filter.FilterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class FilterProperties implements FilterDescriptor {

    private final @NotNull UUID id;
    private @Setter Optional<String> name;
    private @Setter Optional<FilterType> type;
    private @Setter Optional<UUID> nodeId;

    public FilterProperties(UUID id, String name, FilterType type, UUID nodeId) {
        this.id = id;
        this.name = Optional.ofNullable(name);
        this.type = Optional.ofNullable(type);
        this.nodeId = Optional.ofNullable(nodeId);
    }

}
