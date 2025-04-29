package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BytesFixedParameter extends ContractEventParameter<byte[]> {
    private final int byteLength;

    public BytesFixedParameter(boolean indexed, int position, byte[] value, int byteLength) {
        super(ParameterType.BYTES_FIXED, indexed, position, value);
        if (byteLength < 1 || byteLength > 32)
            throw new IllegalArgumentException("Invalid bytes length: " + byteLength);
        if (value.length != byteLength)
            throw new IllegalArgumentException(
                    "Invalid bytes value length: " + value.length + ", expected: " + byteLength);
        this.byteLength = byteLength;
    }
}
