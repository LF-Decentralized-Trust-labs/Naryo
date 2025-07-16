package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.parameter.BytesFixedParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

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
        InstancioApi<BytesFixedParameterDefinition> builder = Instancio.of(BytesFixedParameterDefinition.class);

        return super.buildBase(builder, ParameterType.BYTES_FIXED)
            .set(field(BytesFixedParameterDefinition::getByteLength), this.getByteLength())
            .create();
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
