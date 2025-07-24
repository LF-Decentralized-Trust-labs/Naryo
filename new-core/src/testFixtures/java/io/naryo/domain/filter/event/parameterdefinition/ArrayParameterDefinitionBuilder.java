package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.ArrayParameterDefinition;

public class ArrayParameterDefinitionBuilder
        extends ParameterDefinitionBuilder<
                ArrayParameterDefinitionBuilder, ArrayParameterDefinition> {

    private ParameterDefinition elementType;
    private Integer fixedLength;

    @Override
    public ArrayParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public ArrayParameterDefinition build() {
        return new ArrayParameterDefinition(
                this.getPosition(), this.getElementType(), this.getFixedLength());
    }

    public ArrayParameterDefinitionBuilder withElementType(ParameterDefinition elementType) {
        this.elementType = elementType;
        return this.self();
    }

    public ArrayParameterDefinitionBuilder withFixedLength(Integer fixedLength) {
        this.fixedLength = fixedLength;
        return this.self();
    }

    private ParameterDefinition getElementType() {
        return this.elementType == null
                ? new AddressParameterDefinitionBuilder().build()
                : this.elementType;
    }

    private Integer getFixedLength() {
        return this.fixedLength == null ? 1 : this.fixedLength;
    }
}
