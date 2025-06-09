package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class IntParameter extends ContractEventParameter<Integer> {
    public IntParameter(boolean indexed, int position, Integer value) {
        super(ParameterType.INT, indexed, position, value);
        if (value < 0) {
            throw new IllegalArgumentException("Invalid int value: " + value);
        }
    }
}
