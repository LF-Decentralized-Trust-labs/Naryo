package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;

public final class BoolParameterDefinition extends ParameterDefinition {

    public BoolParameterDefinition() {
        super(ParameterType.BOOL);
    }

    public BoolParameterDefinition(int position, boolean indexed) {
        super(ParameterType.BOOL, position, indexed);
    }
}
