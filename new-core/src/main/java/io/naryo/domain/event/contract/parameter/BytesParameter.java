package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class BytesParameter extends ContractEventParameter<byte[]> {
    public BytesParameter(boolean indexed, int position, byte[] value) {
        super(ParameterType.BYTES, indexed, position, value);
    }
}
