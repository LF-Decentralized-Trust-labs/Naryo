package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class UintParameter extends ContractEventParameter<Integer> {
    public UintParameter(boolean indexed, int position, Integer value) {
        super(ParameterType.UINT, indexed, position, value);
    }
}
