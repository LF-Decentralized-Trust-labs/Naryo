package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.filter.event.ParameterDefinition;
import org.instancio.Instancio;

public abstract class ParameterDefinitionBuilder<T, Y extends ParameterDefinition> {

    private static final boolean DEFAULT_INDEXED = false;

    private Integer position;
    private Boolean indexed;

    public abstract T self();

    public abstract Y build();

    public T withPosition(int position) {
        this.position = position;
        return this.self();
    }

    public T withIndexed(boolean indexed) {
        this.indexed = indexed;
        return this.self();
    }

    protected int getPosition() {
        return this.position == null ? Instancio.create(Integer.class) : this.position;
    }

    protected boolean isIndexed() {
        return this.indexed == null ? DEFAULT_INDEXED : this.indexed;
    }
}
