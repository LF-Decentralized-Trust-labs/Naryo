package io.librevents.domain.filter.event.parameter;

import java.util.Objects;

import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.ParameterType;
import lombok.Getter;

@Getter
public final class ArrayParameterDefinition extends ParameterDefinition {
    private final ParameterDefinition elementType;
    private final Integer fixedLength;

    public ArrayParameterDefinition(
            int position, ParameterDefinition elementType, Integer fixedLength) {
        super(ParameterType.ARRAY, position, false);
        Objects.requireNonNull(elementType, "elementType must not be null");
        this.elementType = elementType;
        this.fixedLength = fixedLength;
    }
}
