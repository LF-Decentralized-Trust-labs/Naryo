package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.util.Set;

@TypeAlias("struct_parameter_definition")
@Getter
public class StructParameterDefinitionPropertiesDocument extends ParameterDefinitionPropertiesDocument {

    @NotNull
    private Set<ParameterDefinitionPropertiesDocument> parameterDefinitions;

}
