package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.common.ParameterType;

public final class StringParameterDefinition extends ParameterDefinition {

    public StringParameterDefinition() {
        super(ParameterType.STRING);
    }

    public StringParameterDefinition(int position) {
        super(ParameterType.STRING, position, false);
    }
}
