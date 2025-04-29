package io.librevents.domain.event.contract;

import java.util.Objects;

import io.librevents.domain.common.ParameterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ContractEventParameter<V> {

    private final ParameterType type;
    private final boolean indexed;
    private final int position;
    private final V value;

    protected ContractEventParameter(ParameterType type, boolean indexed, int position, V value) {
        this.type = type;
        this.indexed = indexed;
        this.position = position;
        this.value = value;

        if (position < 0) {
            throw new IllegalArgumentException("position must be non-negative");
        }
        Objects.requireNonNull(value, "value must not be null");
    }
}
