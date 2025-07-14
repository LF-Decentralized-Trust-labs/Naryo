package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class IndexedParameterDefinitionPropertiesDocument extends ParameterDefinitionPropertiesDocument {

    @NotNull
    private boolean indexed;
}
