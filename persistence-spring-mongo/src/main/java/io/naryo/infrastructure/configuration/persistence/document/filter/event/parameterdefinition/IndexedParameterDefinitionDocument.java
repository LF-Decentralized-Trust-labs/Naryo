package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class IndexedParameterDefinitionDocument extends ParameterDefinitionDocument {

    @NotNull
    private boolean indexed;

    public IndexedParameterDefinitionDocument(ParameterType parameterType, int position, boolean indexed) {
        super(parameterType, position);
        this.indexed = indexed;
    }
}
