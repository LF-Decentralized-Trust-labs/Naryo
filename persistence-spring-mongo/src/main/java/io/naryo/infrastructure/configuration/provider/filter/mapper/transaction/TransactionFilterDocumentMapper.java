package io.naryo.infrastructure.configuration.provider.filter.mapper.transaction;

import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;

import java.util.UUID;

public abstract class TransactionFilterDocumentMapper {

    public static TransactionFilter fromDocument(TransactionFilterDocument props) {
        return new TransactionFilter(
            UUID.fromString(props.getId()),
            new FilterName(props.getName()),
            UUID.fromString(props.getNodeId()),
            props.getIdentifierType(),
            props.getValue(),
            props.getStatuses()
        );
    }
}
