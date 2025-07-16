package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.StringParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class StringParameterDefinitionBuilder extends
    ParameterDefinitionBuilder<StringParameterDefinitionBuilder, StringParameterDefinition> {

    @Override
    public StringParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public StringParameterDefinition build() {
        InstancioApi<StringParameterDefinition> builder = Instancio.of(StringParameterDefinition.class);

        return super.buildBase(builder, ParameterType.STRING).create();
    }
}
