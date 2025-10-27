package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class BytesFixedParameterDefinition extends ParameterDefinition {
    private final int byteLength;

    public BytesFixedParameterDefinition(int byteLength) {
        super(ParameterType.BYTES_FIXED, false);
        validateLength(byteLength);
        this.byteLength = byteLength;
    }

    public BytesFixedParameterDefinition(int byteLength, int position, boolean indexed) {
        super(ParameterType.BYTES_FIXED, position, indexed, false);
        validateLength(byteLength);
        this.byteLength = byteLength;
    }

    private void validateLength(int byteLength) {
        if (byteLength < 1 || byteLength > 32)
            throw new IllegalArgumentException("Invalid bytes length: " + byteLength);
    }
}
