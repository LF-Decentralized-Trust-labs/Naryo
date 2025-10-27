package io.naryo.domain.filter.event;

import io.naryo.domain.common.ParameterType;
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
    private final boolean dynamic;

    protected ParameterDefinition(ParameterType type, boolean dynamic) {
        this.type = type;
        this.position = 0;
        this.indexed = false;
        this.dynamic = dynamic;
    }

    protected ParameterDefinition(
            ParameterType type, int position, boolean indexed, boolean dynamic) {
        this.type = type;
        this.position = position;
        this.indexed = indexed;
        this.dynamic = dynamic;
        if (position < 0) {
            throw new IllegalArgumentException("Position cannot be negative");
        }
    }
}
