package io.naryo.infrastructure.configuration.provider.filter.mapper;

import io.naryo.domain.filter.Filter;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.EventFilterDocumentMapper;
import io.naryo.infrastructure.configuration.provider.filter.mapper.transaction.TransactionFilterDocumentMapper;

public abstract class FilterDocumentMapper {

    public static Filter fromDocument(FilterPropertiesDocument props) {
        return switch (props) {
            case TransactionFilterPropertiesDocument transactionFilterProps ->
                TransactionFilterDocumentMapper.fromDocument(transactionFilterProps);
            case EventFilterPropertiesDocument eventFilterProps ->
                EventFilterDocumentMapper.fromDocument(eventFilterProps);
            default -> throw new IllegalArgumentException("Unknown filter type: " + props.getType());
        };
    }
}
