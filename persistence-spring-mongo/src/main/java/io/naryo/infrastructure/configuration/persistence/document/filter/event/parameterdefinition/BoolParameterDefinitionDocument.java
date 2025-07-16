package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("bool_parameter_definition")
@Getter
public class BoolParameterDefinitionDocument extends IndexedParameterDefinitionDocument {

    public BoolParameterDefinitionDocument(int position, boolean indexed) {
        super(ParameterType.BOOL, position, indexed);
    }
}
