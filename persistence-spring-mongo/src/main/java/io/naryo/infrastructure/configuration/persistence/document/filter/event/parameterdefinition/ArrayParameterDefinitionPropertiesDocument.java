package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("array_parameter_definition")
@Getter
public class ArrayParameterDefinitionPropertiesDocument extends ParameterDefinitionPropertiesDocument {

    @NotNull
    private ParameterDefinitionPropertiesDocument elementType;

    @NotNull
    private int fixedLength;

}
