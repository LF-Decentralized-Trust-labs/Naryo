package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionPropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class EventFilterSpecificationPropertiesDocument {
    @NotNull
    private String eventName;

    @NotNull
    private int correlationId;

    @NotNull
    private List<ParameterDefinitionPropertiesDocument> parameters;

}
