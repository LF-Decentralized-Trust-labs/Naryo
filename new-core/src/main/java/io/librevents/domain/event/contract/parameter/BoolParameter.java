package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class BoolParameter extends ContractEventParameter<Boolean> {
    public BoolParameter(boolean indexed, int position, Boolean value) {
        super(ParameterType.BOOL, indexed, position, value);
    }
}
