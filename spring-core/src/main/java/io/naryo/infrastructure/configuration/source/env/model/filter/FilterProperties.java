package io.naryo.infrastructure.configuration.source.env.model.filter;

import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.filter.FilterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class FilterProperties implements FilterDescriptor {

    private final @Getter @NotNull UUID id;
    private @Getter @Setter @NotBlank String name;
    private final @Getter @NotNull FilterType type;
    private @Getter @Setter @NotNull UUID nodeId;

    public FilterProperties(UUID id, String name, FilterType type, UUID nodeId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.nodeId = nodeId;
    }
}
