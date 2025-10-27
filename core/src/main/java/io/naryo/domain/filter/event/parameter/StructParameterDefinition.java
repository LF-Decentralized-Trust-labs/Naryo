package io.naryo.domain.filter.event.parameter;

import java.util.Objects;
import java.util.Set;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class StructParameterDefinition extends ParameterDefinition {
    private final Set<ParameterDefinition> parameterDefinitions;
    private final boolean dynamic;

    public StructParameterDefinition(int position, Set<ParameterDefinition> parameterDefinitions) {
        super(ParameterType.STRUCT, position, false);
        Objects.requireNonNull(parameterDefinitions, "parameterDefinitions cannot be null");
        if (parameterDefinitions.isEmpty()) {
            throw new IllegalArgumentException("parameterDefinitions cannot be empty");
        }
        this.parameterDefinitions = parameterDefinitions;
        this.dynamic = parameterDefinitions.stream().anyMatch(ParameterDefinition::isDynamic);
    }

    @Override
    public boolean isDynamic() {
        return this.dynamic;
    }
}
