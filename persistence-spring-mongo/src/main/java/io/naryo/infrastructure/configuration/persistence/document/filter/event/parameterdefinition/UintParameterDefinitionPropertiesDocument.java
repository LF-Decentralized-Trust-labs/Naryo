package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("uint_parameter_definition")
@Getter
public class UintParameterDefinitionPropertiesDocument extends IndexedParameterDefinitionPropertiesDocument {

    @NotNull
    private int bitSize;

}
