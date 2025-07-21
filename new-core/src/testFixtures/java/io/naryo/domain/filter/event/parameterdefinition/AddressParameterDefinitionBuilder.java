package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;

public class AddressParameterDefinitionBuilder
        extends ParameterDefinitionBuilder<
                AddressParameterDefinitionBuilder, AddressParameterDefinition> {

    @Override
    public AddressParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public AddressParameterDefinition build() {
        return new AddressParameterDefinition(this.getPosition(), this.isIndexed());
    }
}
