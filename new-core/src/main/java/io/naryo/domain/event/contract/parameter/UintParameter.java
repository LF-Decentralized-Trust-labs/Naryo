package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class UintParameter extends ContractEventParameter<Integer> {
    public UintParameter(boolean indexed, int position, Integer value) {
        super(ParameterType.UINT, indexed, position, value);
    }
}
