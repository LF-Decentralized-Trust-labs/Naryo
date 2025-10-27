package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;

public final class StringParameterDefinition extends ParameterDefinition {

    public StringParameterDefinition() {
        super(ParameterType.STRING);
    }

    public StringParameterDefinition(int position, boolean isIndexed) {
        super(ParameterType.STRING, position, isIndexed);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
