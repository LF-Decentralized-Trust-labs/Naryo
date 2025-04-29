package io.librevents.domain.filter.event.parameter;

import java.util.Objects;
import java.util.Set;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class StructParameterDefinition extends ParameterDefinition {
    private final Set<ParameterDefinition> parameterDefinitions;

    public StructParameterDefinition(int position, Set<ParameterDefinition> parameterDefinitions) {
        super(ParameterType.STRUCT, position, false);
        Objects.requireNonNull(parameterDefinitions, "parameterDefinitions cannot be null");
        if (parameterDefinitions.isEmpty()) {
            throw new IllegalArgumentException("parameterDefinitions cannot be empty");
        }
        this.parameterDefinitions = parameterDefinitions;
    }
}
