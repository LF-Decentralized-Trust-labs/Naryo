package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("int_parameter_definition")
@Getter
public class IntParameterDefinitionDocument extends IndexedParameterDefinitionDocument {

    @NotNull
    private int bitSize;

    public IntParameterDefinitionDocument(int position, boolean indexed, int bitSize) {
        super(ParameterType.INT, position, indexed);
        this.bitSize = bitSize;
    }
}
