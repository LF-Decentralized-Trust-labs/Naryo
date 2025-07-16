package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("bytes_fixed_parameter_definition")
@Getter
public class BytesFixedParameterDefinitionDocument extends IndexedParameterDefinitionDocument {

    @NotNull
    private int byteLength;

    public BytesFixedParameterDefinitionDocument(int position, boolean indexed, int byteLength) {
        super(ParameterType.BYTES_FIXED, position, indexed);
        this.byteLength = byteLength;
    }
}
