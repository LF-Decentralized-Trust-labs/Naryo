package io.naryo.domain.event.contract.parameter;

import java.util.List;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class StructParameter extends ContractEventParameter<List<ContractEventParameter<?>>> {

    public StructParameter(boolean indexed, int position, List<ContractEventParameter<?>> value) {
        super(ParameterType.STRUCT, indexed, position, value);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("StructParameter value cannot be empty");
        }
    }
}
