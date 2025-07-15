package io.naryo.infrastructure.configuration.provider.filter.mapper;

import io.naryo.domain.filter.Filter;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.EventFilterDocumentMapper;
import io.naryo.infrastructure.configuration.provider.filter.mapper.transaction.TransactionFilterDocumentMapper;

public abstract class FilterDocumentMapper {

    public static Filter fromDocument(FilterDocument props) {
        return switch (props) {
            case TransactionFilterDocument transactionFilterProps ->
                TransactionFilterDocumentMapper.fromDocument(transactionFilterProps);
            case EventFilterDocument eventFilterProps ->
                EventFilterDocumentMapper.fromDocument(eventFilterProps);
            default -> throw new IllegalArgumentException("Unknown filter type: " + props.getType());
        };
    }
}
