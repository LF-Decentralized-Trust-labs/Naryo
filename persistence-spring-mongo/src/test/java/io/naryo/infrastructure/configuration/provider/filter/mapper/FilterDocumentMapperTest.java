package io.naryo.infrastructure.configuration.provider.filter.mapper;

import fixtures.persistence.filter.event.GlobalEventFilterDocumentBuilder;
import fixtures.persistence.filter.transaction.TransactionFilterDocumentBuilder;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.filter.transaction.TransactionFilterBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.EventFilterDocumentMapper;
import io.naryo.infrastructure.configuration.provider.filter.mapper.transaction.TransactionFilterDocumentMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class FilterDocumentMapperTest {

    @Test
    void testFromDocumentForTransactionFilter() {
        TransactionFilterDocument document = new TransactionFilterDocumentBuilder().build();
        TransactionFilter mockResult = new TransactionFilterBuilder().build();
        Filter result;

        try (MockedStatic<TransactionFilterDocumentMapper> mockedMapper =
                 Mockito.mockStatic(TransactionFilterDocumentMapper.class)) {
            mockedMapper.when(() -> TransactionFilterDocumentMapper.fromDocument(document)).thenReturn(mockResult);
            result = FilterDocumentMapper.fromDocument(document);
        }

        assertEquals(mockResult, result);
    }

    @Test
    void testFromDocumentForEventFilter() {
        GlobalEventFilterDocument document = new GlobalEventFilterDocumentBuilder().build();
        EventFilter mockResult = new GlobalEventFilterBuilder().build();
        Filter result;

        try (MockedStatic<EventFilterDocumentMapper> mockedMapper =
                 Mockito.mockStatic(EventFilterDocumentMapper.class)) {
            mockedMapper.when(() -> EventFilterDocumentMapper.fromDocument(document)).thenReturn(mockResult);
            result = FilterDocumentMapper.fromDocument(document);
        }

        assertEquals(mockResult, result);
    }

    @Test
    void testFromDocumentForUnknownFilter() {
        class UnknownFilterDocument extends FilterDocument {
            public UnknownFilterDocument() {
                super(
                    Instancio.create(String.class),
                    Instancio.create(String.class),
                    FilterType.TRANSACTION,
                    Instancio.create(String.class)
                );
            }
        }
        UnknownFilterDocument document = new UnknownFilterDocument();

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> FilterDocumentMapper.fromDocument(document),
            "Unknown filter type: " + document.getType());
    }
}
