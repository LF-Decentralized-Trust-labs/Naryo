package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.ParameterType;

public final class BoolParameterDefinition extends ParameterDefinition {
    public BoolParameterDefinition(int position, boolean indexed) {
        super(ParameterType.BOOL, position, indexed);
    }
}
