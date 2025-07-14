package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.event.EventFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterPropertiesDocument;

public abstract class EventFilterDocumentMapper {

    public static EventFilter fromDocument(EventFilterPropertiesDocument props) {
        return switch (props) {
            case GlobalEventFilterPropertiesDocument globalEventFilterProps ->
                GlobalEventFilterDocumentMapper.fromDocument(globalEventFilterProps);
            case ContractEventFilterPropertiesDocument contractEventFilterProps ->
                ContractEventFilterDocumentMapper.fromDocument(contractEventFilterProps);
            default ->
                throw new IllegalArgumentException("Unknown event filter scope: " + props.getScope());
        };
    }
}
