package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.common.ParameterType;
import lombok.Getter;

@Getter
public final class BytesFixedParameterDefinition extends ParameterDefinition {
    private final int byteLength;

    public BytesFixedParameterDefinition(int byteLength) {
        super(ParameterType.BYTES_FIXED);
        validateLength(byteLength);
        this.byteLength = byteLength;
    }

    public BytesFixedParameterDefinition(int byteLength, int position, boolean indexed) {
        super(ParameterType.BYTES_FIXED, position, indexed);
        validateLength(byteLength);
        this.byteLength = byteLength;
    }

    private void validateLength(int byteLength) {
        if (byteLength < 1 || byteLength > 32)
            throw new IllegalArgumentException("Invalid bytes length: " + byteLength);
    }
}
