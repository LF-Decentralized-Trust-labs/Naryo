package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.common.ParameterType;

public final class BoolParameterDefinition extends ParameterDefinition {

    public BoolParameterDefinition() {
        super(ParameterType.BOOL);
    }

    public BoolParameterDefinition(int position, boolean indexed) {
        super(ParameterType.BOOL, position, indexed);
    }
}
