package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("array_parameter_definition")
@Getter
public class ArrayParameterDefinitionDocument extends ParameterDefinitionDocument {

    @NotNull
    private ParameterDefinitionDocument elementType;

    @NotNull
    private int fixedLength;

}
