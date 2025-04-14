package io.librevents.domain.event.contract.parameter;

import java.util.List;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class ArrayParameter<V extends ContractEventParameter<?>>
        extends ContractEventParameter<List<V>> {

    public ArrayParameter(boolean indexed, int position, List<V> value) {
        super(ParameterType.ARRAY, indexed, position, value);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("ArrayParameter value cannot be empty");
        }
    }
}
