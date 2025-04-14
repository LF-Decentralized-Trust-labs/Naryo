package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.common.ParameterType;
import lombok.Getter;

@Getter
public final class IntParameterDefinition extends ParameterDefinition {
    private final int bitSize;

    public IntParameterDefinition(int bitSize, int position, boolean indexed) {
        super(ParameterType.INT, position, indexed);
        if (bitSize < 8 || bitSize > 256 || bitSize % 8 != 0)
            throw new IllegalArgumentException("Invalid int bit size: " + bitSize);
        this.bitSize = bitSize;
    }
}
