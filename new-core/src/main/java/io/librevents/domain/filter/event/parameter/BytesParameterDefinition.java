package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.ParameterType;

public final class BytesParameterDefinition extends ParameterDefinition {
    public BytesParameterDefinition(int position) {
        super(ParameterType.BYTES, position, false);
    }
}
