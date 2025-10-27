package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;

public final class AddressParameterDefinition extends ParameterDefinition {

    public AddressParameterDefinition() {
        super(ParameterType.ADDRESS);
    }

    public AddressParameterDefinition(int position, boolean indexed) {
        super(ParameterType.ADDRESS, position, indexed);
    }

    @Override
    public boolean isDynamic() {
        return false;
    }
}
