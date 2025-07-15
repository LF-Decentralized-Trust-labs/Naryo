package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.CorrelationId;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterSpecificationDocument;

import java.util.stream.Collectors;

public abstract class EventFilterSpecificationDocumentMapper {

    public static EventFilterSpecification fromDocument(EventFilterSpecificationDocument props) {
        return new EventFilterSpecification(
            new EventName(props.getEventName()),
            new CorrelationId(props.getCorrelationId()),
            props.getParameters().stream()
                .map(ParameterDefinitionDocumentMapper::fromDocument)
                .collect(Collectors.toSet())
        );
    }
}
