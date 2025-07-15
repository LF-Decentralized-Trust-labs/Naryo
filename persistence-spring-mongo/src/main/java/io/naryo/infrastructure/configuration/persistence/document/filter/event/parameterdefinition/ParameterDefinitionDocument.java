package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import io.naryo.domain.common.ParameterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public abstract class ParameterDefinitionDocument {

    @NotNull
    private ParameterType parameterType;

    @NotNull
    private int position;

}
