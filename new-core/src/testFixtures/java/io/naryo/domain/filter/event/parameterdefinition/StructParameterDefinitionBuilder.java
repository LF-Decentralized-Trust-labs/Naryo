package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.StructParameterDefinition;

import java.util.Set;

public class StructParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<StructParameterDefinitionBuilder, StructParameterDefinition> {

    private Set<ParameterDefinition> parameterDefinitions;

    @Override
    public StructParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public StructParameterDefinition build() {
        return new StructParameterDefinition(this.getPosition(), this.getParameterDefinitions());
    }

    public StructParameterDefinitionBuilder withParameterDefinitions(Set<ParameterDefinition> parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
        return this.self();
    }

    private Set<ParameterDefinition> getParameterDefinitions() {
        return this.parameterDefinitions == null
            ? Set.of(new AddressParameterDefinitionBuilder().build())
            : this.parameterDefinitions;
    }
}
