package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.filter.event.ParameterDefinition;
import lombok.Getter;

@Getter
public final class UintParameterDefinition extends ParameterDefinition {
    private final int bitSize;

    public UintParameterDefinition(int bitSize) {
        super(ParameterType.UINT);
        validateBitSize(bitSize);
        this.bitSize = bitSize;
    }

    public UintParameterDefinition(int bitSize, int position, boolean indexed) {
        super(ParameterType.UINT, position, indexed);
        validateBitSize(bitSize);
        this.bitSize = bitSize;
    }

    private void validateBitSize(int bitSize) {
        if (bitSize < 8 || bitSize > 256 || bitSize % 8 != 0)
            throw new IllegalArgumentException("Invalid uint bit size: " + bitSize);
    }
}
