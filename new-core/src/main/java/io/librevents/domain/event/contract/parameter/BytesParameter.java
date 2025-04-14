package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class BytesParameter extends ContractEventParameter<byte[]> {
    public BytesParameter(boolean indexed, int position, byte[] value) {
        super(ParameterType.BYTES, indexed, position, value);
    }
}
