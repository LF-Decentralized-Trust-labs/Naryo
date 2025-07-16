package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.BoolParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class BoolParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<BoolParameterDefinitionBuilder, BoolParameterDefinition> {

    @Override
    public BoolParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public BoolParameterDefinition build() {
        InstancioApi<BoolParameterDefinition> builder = Instancio.of(BoolParameterDefinition.class);

        return super.buildBase(builder, ParameterType.BOOL).create();
    }
}
