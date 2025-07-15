package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class EventFilterSpecificationDocument {
    @NotNull
    private String eventName;

    @NotNull
    private int correlationId;

    @NotNull
    private List<ParameterDefinitionDocument> parameters;

}
