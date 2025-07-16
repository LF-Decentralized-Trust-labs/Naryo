package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("bytes_parameter_definition")
@Getter
public class BytesParameterDefinitionDocument extends ParameterDefinitionDocument {

    public BytesParameterDefinitionDocument(int position) {
        super(ParameterType.BYTES, position);
    }
}
