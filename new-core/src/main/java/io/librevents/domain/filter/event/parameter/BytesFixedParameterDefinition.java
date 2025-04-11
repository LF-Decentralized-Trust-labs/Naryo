package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.ParameterType;
import lombok.Getter;

@Getter
public final class BytesFixedParameterDefinition extends ParameterDefinition {
    private final int byteLength;

    public BytesFixedParameterDefinition(int byteLength, int position, boolean indexed) {
        super(ParameterType.BYTES_FIXED, position, indexed);
        if (byteLength < 1 || byteLength > 32)
            throw new IllegalArgumentException("Invalid bytes length: " + byteLength);
        this.byteLength = byteLength;
    }
}
