package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("uint_parameter_definition")
@Getter
public class UintParameterDefinitionDocument extends IndexedParameterDefinitionDocument {

    @NotNull
    private int bitSize;

    public UintParameterDefinitionDocument(int position, boolean indexed, int bitSize) {
        super(ParameterType.UINT, position, indexed);
        this.bitSize = bitSize;
    }
}
