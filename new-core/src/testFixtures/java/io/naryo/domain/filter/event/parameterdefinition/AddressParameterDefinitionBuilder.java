package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class AddressParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<AddressParameterDefinitionBuilder, AddressParameterDefinition> {

    @Override
    public AddressParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public AddressParameterDefinition build() {
        InstancioApi<AddressParameterDefinition> builder = Instancio.of(AddressParameterDefinition.class);

        return super.buildBase(builder, ParameterType.ADDRESS).create();
    }
}
