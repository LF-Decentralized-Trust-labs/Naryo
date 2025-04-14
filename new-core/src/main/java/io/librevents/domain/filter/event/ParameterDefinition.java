package io.librevents.domain.filter.event;

import io.librevents.domain.common.ParameterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ParameterDefinition {
    private final ParameterType type;
    private final int position;
    private final boolean indexed;

    public ParameterDefinition(ParameterType type, int position, boolean indexed) {
        this.type = type;
        this.position = position;
        this.indexed = indexed;
        if (position < 0) {
            throw new IllegalArgumentException("Position cannot be negative");
        }
    }
}
