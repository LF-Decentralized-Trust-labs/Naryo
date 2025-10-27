package io.naryo.domain.filter.event.parameter;

import java.util.Objects;
import java.util.Set;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class StructParameterDefinition extends ParameterDefinition {
    private final Set<ParameterDefinition> parameterDefinitions;

    public StructParameterDefinition(int position, Set<ParameterDefinition> parameterDefinitions) {
        super(
                ParameterType.STRUCT,
                position,
                false,
                parameterDefinitions != null
                        && parameterDefinitions.stream().anyMatch(ParameterDefinition::isDynamic));
        Objects.requireNonNull(parameterDefinitions, "parameterDefinitions cannot be null");
        if (parameterDefinitions.isEmpty()) {
            throw new IllegalArgumentException("parameterDefinitions cannot be empty");
        }
        this.parameterDefinitions = parameterDefinitions;
    }
}
