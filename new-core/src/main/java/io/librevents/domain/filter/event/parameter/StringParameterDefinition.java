package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.filter.event.ParameterDefinition;

public final class StringParameterDefinition extends ParameterDefinition {

    public StringParameterDefinition() {
        super(ParameterType.STRING);
    }

    public StringParameterDefinition(int position) {
        super(ParameterType.STRING, position, false);
    }
}
