package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;

public final class BytesParameterDefinition extends ParameterDefinition {

    public BytesParameterDefinition() {
        super(ParameterType.BYTES);
    }

    public BytesParameterDefinition(int position, boolean isIndexed) {
        super(ParameterType.BYTES, position, isIndexed);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
