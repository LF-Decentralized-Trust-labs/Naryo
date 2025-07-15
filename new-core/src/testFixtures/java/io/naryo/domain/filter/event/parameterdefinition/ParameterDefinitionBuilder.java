package io.naryo.domain.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public abstract class ParameterDefinitionBuilder<T, Y extends ParameterDefinition> {

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

    protected InstancioApi<Y> buildBase(InstancioApi<Y> builder, ParameterType parameterType) {
        return builder
            .set(field(ParameterDefinition::getPosition), this.getPosition())
            .set(field(ParameterDefinition::isIndexed), this.isIndexed())
            .set(field(ParameterDefinition::getType), parameterType);
    }

    private int getPosition() {
        return this.position == null
            ? Instancio.create(Integer.class)
            : this.position;
    }

    private boolean isIndexed() {
        return this.indexed == null
            ? Instancio.create(Boolean.class)
            : this.indexed;
    }
}
