package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.common.ParameterType;

public final class AddressParameterDefinition extends ParameterDefinition {
    public AddressParameterDefinition(int position, boolean indexed) {
        super(ParameterType.ADDRESS, position, indexed);
    }
}
