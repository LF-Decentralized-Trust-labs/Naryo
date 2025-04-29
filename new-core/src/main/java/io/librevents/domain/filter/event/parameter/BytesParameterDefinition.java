package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.filter.event.ParameterDefinition;

public final class BytesParameterDefinition extends ParameterDefinition {

    public BytesParameterDefinition() {
        super(ParameterType.BYTES);
    }

    public BytesParameterDefinition(int position) {
        super(ParameterType.BYTES, position, false);
    }
}
