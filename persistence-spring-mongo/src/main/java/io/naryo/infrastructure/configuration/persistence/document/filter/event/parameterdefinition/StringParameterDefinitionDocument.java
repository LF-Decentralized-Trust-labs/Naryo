package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("string_parameter_definition")
@Getter
public class StringParameterDefinitionDocument extends ParameterDefinitionDocument {

    public StringParameterDefinitionDocument(int position) {
        super(ParameterType.STRING, position);
    }
}
