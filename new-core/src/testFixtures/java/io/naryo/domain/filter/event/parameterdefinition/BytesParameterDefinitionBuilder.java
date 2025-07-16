package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.BytesParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class BytesParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<BytesParameterDefinitionBuilder, BytesParameterDefinition> {

    @Override
    public BytesParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public BytesParameterDefinition build() {
        InstancioApi<BytesParameterDefinition> builder = Instancio.of(BytesParameterDefinition.class);

        return super.buildBase(builder, ParameterType.BYTES).create();
    }
}
