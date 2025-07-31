package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class BoolParameter extends ContractEventParameter<Boolean> {
    public BoolParameter(boolean indexed, int position, Boolean value) {
        super(ParameterType.BOOL, indexed, position, value);
    }
}
