package io.naryo.infrastructure.configuration.provider.filter.mapper.transaction;

import fixtures.persistence.filter.transaction.TransactionFilterDocumentBuilder;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.FilterDocumentMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class TransactionFilterDocumentMapperTest {

    @Test
    void testFromDocument() {
        TransactionFilterDocument document = new TransactionFilterDocumentBuilder().build();
        TransactionFilter expected = new TransactionFilterBuilder()
            .withId(UUID.fromString(document.getId()))
            .withName(new FilterName(document.getName()))
            .withNodeId(UUID.fromString(document.getNodeId()))
            .withIdentifierType(document.getIdentifierType())
            .withValue(document.getValue())
            .withStatuses(document.getStatuses())
            .build();

        Filter result = FilterDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }
}
