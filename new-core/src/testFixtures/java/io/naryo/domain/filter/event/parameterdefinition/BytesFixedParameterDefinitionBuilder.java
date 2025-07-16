package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.parameter.BytesFixedParameterDefinition;

public class BytesFixedParameterDefinitionBuilder
    extends ParameterDefinitionBuilder<BytesFixedParameterDefinitionBuilder, BytesFixedParameterDefinition> {

    private static final int DEFAULT_BYTE_LENGTH = 1;

    private Integer byteLength;

    @Override
    public BytesFixedParameterDefinitionBuilder self() {
        return this;
    }

    @Override
    public BytesFixedParameterDefinition build() {
        return new BytesFixedParameterDefinition(this.getByteLength(), this.getPosition(), this.isIndexed());
    }

    public BytesFixedParameterDefinitionBuilder withByteLength(int byteLength) {
        this.byteLength = byteLength;
        return this.self();
    }

    private int getByteLength() {
        return this.byteLength == null
            ? DEFAULT_BYTE_LENGTH
            : this.byteLength;
    }
}
