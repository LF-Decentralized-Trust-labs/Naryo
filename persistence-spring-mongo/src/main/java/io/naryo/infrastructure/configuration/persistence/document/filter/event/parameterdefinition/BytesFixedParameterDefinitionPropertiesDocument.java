package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("bytes_fixed_parameter_definition")
@Getter
public class BytesFixedParameterDefinitionPropertiesDocument extends IndexedParameterDefinitionPropertiesDocument {

    @NotNull
    private int byteLength;

}
