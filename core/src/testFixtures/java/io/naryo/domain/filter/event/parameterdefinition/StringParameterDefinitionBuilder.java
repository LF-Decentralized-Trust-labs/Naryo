package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.StringParameterDefinition;

public class StringParameterDefinitionBuilder
        extends ParameterDefinitionBuilder<
                StringParameterDefinitionBuilder, StringParameterDefinition> {

    @Override
    public StringParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public StringParameterDefinition build() {
        return new StringParameterDefinition(this.getPosition());
    }
}
