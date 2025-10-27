package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class IntParameterDefinition extends ParameterDefinition {
    private final int bitSize;

    public IntParameterDefinition(int bitSize) {
        super(ParameterType.INT, false);
        validateBitSize(bitSize);
        this.bitSize = bitSize;
    }

    public IntParameterDefinition(int bitSize, int position, boolean indexed) {
        super(ParameterType.INT, position, indexed, false);
        validateBitSize(bitSize);
        this.bitSize = bitSize;
    }

    private void validateBitSize(int bitSize) {
        if (bitSize < 8 || bitSize > 256 || bitSize % 8 != 0)
            throw new IllegalArgumentException("Invalid int bit size: " + bitSize);
    }
}
