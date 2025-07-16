package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.ArrayParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class ArrayParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<ArrayParameterDefinitionBuilder, ArrayParameterDefinition> {

    private ParameterDefinition elementType;
    private Integer fixedLength;

    @Override
    public ArrayParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public ArrayParameterDefinition build() {
        InstancioApi<ArrayParameterDefinition> builder = Instancio.of(ArrayParameterDefinition.class);

        return super.buildBase(builder, ParameterType.ARRAY)
            .set(field(ArrayParameterDefinition::getElementType), this.getElementType())
            .set(field(ArrayParameterDefinition::getFixedLength), this.getFixedLength())
            .create();
    }

    private ParameterDefinition getElementType() {
        return this.elementType == null
            ? new AddressParameterDefinitionBuilder().build()
            : this.elementType;
    }

    private Integer getFixedLength() {
        return this.fixedLength == null
            ? 1
            : this.fixedLength;
    }
}
