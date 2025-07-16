package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import fixtures.persistence.filter.event.ContractEventFilterDocumentBuilder;
import fixtures.persistence.filter.event.EventFilterSpecificationDocumentBuilder;
import fixtures.persistence.filter.event.EventFilterVisibilityConfigurationDocumentBuilder;
import fixtures.persistence.filter.event.GlobalEventFilterDocumentBuilder;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.ContractEventFilterBuilder;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterDocument;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class EventFilterDocumentMapperTest {

    @Test
    void testFromDocumentForGlobalEventFilter() {
        GlobalEventFilterDocument document = new GlobalEventFilterDocumentBuilder().build();
        EventFilter mockResult = new GlobalEventFilterBuilder().build();
        Filter result;

        try (MockedStatic<GlobalEventFilterDocumentMapper> mockedMapper =
                 Mockito.mockStatic(GlobalEventFilterDocumentMapper.class)) {
            mockedMapper.when(() -> GlobalEventFilterDocumentMapper.fromDocument(document)).thenReturn(mockResult);
            result = EventFilterDocumentMapper.fromDocument(document);
        }

        assertEquals(mockResult, result);
    }

    @Test
    void testFromDocumentForContractEventFilter() {
        ContractEventFilterDocument document = new ContractEventFilterDocumentBuilder().build();
        EventFilter mockResult = new ContractEventFilterBuilder().build();
        Filter result;

        try (MockedStatic<ContractEventFilterDocumentMapper> mockedMapper =
                 Mockito.mockStatic(ContractEventFilterDocumentMapper.class)) {
            mockedMapper.when(() -> ContractEventFilterDocumentMapper.fromDocument(document)).thenReturn(mockResult);
            result = EventFilterDocumentMapper.fromDocument(document);
        }

        assertEquals(mockResult, result);
    }

    @Test
    void testFromDocumentForUnknownFilter() {
        class UnknownFilterDocument extends EventFilterDocument {
            public UnknownFilterDocument() {
                super(
                    Instancio.create(String.class),
                    Instancio.create(String.class),
                    Instancio.create(String.class),
                    EventFilterScope.CONTRACT,
                    new EventFilterSpecificationDocumentBuilder().build(),
                    Instancio.createList(ContractEventStatus.class),
                    null,
                    new EventFilterVisibilityConfigurationDocumentBuilder().build()
                );
            }
        }
        UnknownFilterDocument document = new UnknownFilterDocument();

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> EventFilterDocumentMapper.fromDocument(document),
            "Unknown event filter with scope: " + document.getScope());
    }
}
