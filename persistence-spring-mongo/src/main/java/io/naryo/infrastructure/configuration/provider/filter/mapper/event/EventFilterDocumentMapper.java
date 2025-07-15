package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.event.EventFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterDocument;

public abstract class EventFilterDocumentMapper {

    public static EventFilter fromDocument(EventFilterDocument props) {
        return switch (props) {
            case GlobalEventFilterDocument globalEventFilterProps ->
                GlobalEventFilterDocumentMapper.fromDocument(globalEventFilterProps);
            case ContractEventFilterDocument contractEventFilterProps ->
                ContractEventFilterDocumentMapper.fromDocument(contractEventFilterProps);
            default ->
                throw new IllegalArgumentException("Unknown event filter scope: " + props.getScope());
        };
    }
}
