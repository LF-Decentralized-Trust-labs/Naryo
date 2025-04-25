package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.filter.event.ParameterDefinition;

public final class AddressParameterDefinition extends ParameterDefinition {

    public AddressParameterDefinition() {
        super(ParameterType.ADDRESS);
    }

    public AddressParameterDefinition(int position, boolean indexed) {
        super(ParameterType.ADDRESS, position, indexed);
    }
}
