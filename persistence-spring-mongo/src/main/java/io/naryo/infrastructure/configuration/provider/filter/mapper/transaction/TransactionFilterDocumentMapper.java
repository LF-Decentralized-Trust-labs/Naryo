package io.naryo.infrastructure.configuration.provider.filter.mapper.transaction;

import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterPropertiesDocument;

import java.util.UUID;

public abstract class TransactionFilterDocumentMapper {

    public static TransactionFilter fromDocument(TransactionFilterPropertiesDocument props) {
        return new TransactionFilter(
            UUID.fromString(props.getUuid()),
            new FilterName(props.getName()),
            UUID.fromString(props.getNodeId()),
            props.getIdentifierType(),
            props.getValue(),
            props.getStatuses()
        );
    }
}
