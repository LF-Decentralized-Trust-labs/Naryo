package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.BoolParameterDefinition;

public class BoolParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<BoolParameterDefinitionBuilder, BoolParameterDefinition> {

    @Override
    public BoolParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public BoolParameterDefinition build() {
        return new BoolParameterDefinition(this.getPosition(), this.isIndexed());
    }
}
