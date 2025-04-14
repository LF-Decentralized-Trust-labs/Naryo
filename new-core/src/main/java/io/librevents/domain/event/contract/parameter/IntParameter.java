package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class IntParameter extends ContractEventParameter<Integer> {
    public IntParameter(boolean indexed, int position, Integer value) {
        super(ParameterType.INT, indexed, position, value);
        if (value < 0) {
            throw new IllegalArgumentException("Invalid int value: " + value);
        }
    }
}
