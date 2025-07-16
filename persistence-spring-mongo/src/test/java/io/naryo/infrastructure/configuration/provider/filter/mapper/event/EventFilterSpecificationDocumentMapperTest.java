package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import fixtures.persistence.filter.event.EventFilterSpecificationDocumentBuilder;
import fixtures.persistence.filter.event.parameterdefinition.AddressParameterDefinitionDocumentBuilder;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.CorrelationId;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterSpecificationBuilder;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameterdefinition.AddressParameterDefinitionBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterSpecificationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class EventFilterSpecificationDocumentMapperTest {

    @Test
    void testFromDocument() {
        ParameterDefinitionDocument parameter1 = new AddressParameterDefinitionDocumentBuilder().build();
        ParameterDefinitionDocument parameter2 = new AddressParameterDefinitionDocumentBuilder().build();
        EventFilterSpecificationDocument document = new EventFilterSpecificationDocumentBuilder()
            .withParameters(List.of(parameter1, parameter2))
            .build();

        ParameterDefinition mockParameter1 = new AddressParameterDefinitionBuilder().build();
        ParameterDefinition mockParameter2 = new AddressParameterDefinitionBuilder().build();

        EventFilterSpecification expected = new EventFilterSpecificationBuilder()
            .withEventName(new EventName(document.getEventName()))
            .withCorrelationId(new CorrelationId(document.getCorrelationId()))
            .withParameters(Set.of(mockParameter1, mockParameter2))
            .build();

        EventFilterSpecification result;
        try (MockedStatic<ParameterDefinitionDocumentMapper> mockedSpecificationMapper =
                 Mockito.mockStatic(ParameterDefinitionDocumentMapper.class)) {

            mockedSpecificationMapper.when(
                () -> ParameterDefinitionDocumentMapper.fromDocument(parameter1)
            ).thenReturn(mockParameter1);

            mockedSpecificationMapper.when(
                () -> ParameterDefinitionDocumentMapper.fromDocument(parameter2)
            ).thenReturn(mockParameter2);

            result = EventFilterSpecificationDocumentMapper.fromDocument(document);
        }

        assertEquals(expected, result);
    }
}
