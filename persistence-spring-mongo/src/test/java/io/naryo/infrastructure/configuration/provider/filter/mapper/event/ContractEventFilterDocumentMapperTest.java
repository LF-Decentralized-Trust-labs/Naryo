package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import fixtures.persistence.filter.event.ContractEventFilterDocumentBuilder;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate.SyncStateDocumentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ContractEventFilterDocumentMapperTest {

    @Test
    void testFromDocument() {
        ContractEventFilterDocument document = new ContractEventFilterDocumentBuilder().build();
        EventFilterSpecification mockEventFilterSpecification = new EventFilterSpecificationBuilder().build();
        SyncState mockSyncState = new BlockActiveSyncStateBuilder().build();
        EventFilterVisibilityConfiguration mockVisibilityConfiguration = new EventFilterVisibilityConfigurationBuilder().build();

        ContractEventFilter expected = new ContractEventFilterBuilder()
            .withId(UUID.fromString(document.getId()))
            .withName(new FilterName(document.getName()))
            .withNodeId(UUID.fromString(document.getNodeId()))
            .withSpecification(mockEventFilterSpecification)
            .withStatuses(document.getStatuses())
            .withSyncState(mockSyncState)
            .withVisibilityConfiguration(mockVisibilityConfiguration)
            .withContractAddress(document.getContractAddress())
            .build();

        Filter result = this.executeMapperWithMocked(
            document,
            mockEventFilterSpecification,
            mockSyncState,
            mockVisibilityConfiguration
        );

        assertEquals(expected, result);
    }

    private Filter executeMapperWithMocked(ContractEventFilterDocument document,
                                           EventFilterSpecification mockEventFilterSpecification,
                                           SyncState mockSyncState,
                                           EventFilterVisibilityConfiguration mockVisibilityConfiguration) {
        try (MockedStatic<EventFilterSpecificationDocumentMapper> mockedSpecificationMapper =
                 Mockito.mockStatic(EventFilterSpecificationDocumentMapper.class);
             MockedStatic<SyncStateDocumentMapper> mockedSyncStateMapper =
                 Mockito.mockStatic(SyncStateDocumentMapper.class);
             MockedStatic<EventFilterVisibilityConfigurationDocumentMapper> mockedVisibilityConfigurationMapper =
                 Mockito.mockStatic(EventFilterVisibilityConfigurationDocumentMapper.class)) {

            mockedSpecificationMapper.when(
                () -> EventFilterSpecificationDocumentMapper.fromDocument(document.getSpecification())
            ).thenReturn(mockEventFilterSpecification);

            mockedSyncStateMapper.when(
                () -> SyncStateDocumentMapper.fromDocument(document.getSyncState())
            ).thenReturn(mockSyncState);

            mockedVisibilityConfigurationMapper.when(
                () -> EventFilterVisibilityConfigurationDocumentMapper.fromDocument(document.getVisibilityConfiguration())
            ).thenReturn(mockVisibilityConfiguration);

            return ContractEventFilterDocumentMapper.fromDocument(document);
        }
    }
}
