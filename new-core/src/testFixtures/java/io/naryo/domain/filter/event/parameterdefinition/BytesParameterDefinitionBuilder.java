package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.BytesParameterDefinition;

public class BytesParameterDefinitionBuilder
        extends ParameterDefinitionBuilder<
                BytesParameterDefinitionBuilder, BytesParameterDefinition> {

    @Override
    public BytesParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public BytesParameterDefinition build() {
        return new BytesParameterDefinition(this.getPosition());
    }
}
