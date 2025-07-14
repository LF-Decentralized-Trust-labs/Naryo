package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("int_parameter_definition")
@Getter
public class IntParameterDefinitionPropertiesDocument extends IndexedParameterDefinitionPropertiesDocument {

    @NotNull
    private int bitSize;

}
