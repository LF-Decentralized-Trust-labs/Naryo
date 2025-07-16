package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import fixtures.persistence.filter.event.parameterdefinition.*;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameterdefinition.*;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ParameterDefinitionDocumentMapperTest {

    @Test
    void testFromDocumentForAddress() {
        AddressParameterDefinitionDocument document = new AddressParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new AddressParameterDefinitionBuilder()
            .withIndexed(document.isIndexed())
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForArray() {
        AddressParameterDefinitionDocument elementTypeDocument = new AddressParameterDefinitionDocumentBuilder().build();
        ArrayParameterDefinitionDocument document = new ArrayParameterDefinitionDocumentBuilder()
            .withElementType(elementTypeDocument)
            .build();

        ParameterDefinition expectedElementType = new AddressParameterDefinitionBuilder()
            .withIndexed(elementTypeDocument.isIndexed())
            .withPosition(elementTypeDocument.getPosition())
            .build();

        ParameterDefinition expected = new ArrayParameterDefinitionBuilder()
            .withPosition(document.getPosition())
            .withElementType(expectedElementType)
            .withFixedLength(document.getFixedLength())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForBool() {
        BoolParameterDefinitionDocument document = new BoolParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new BoolParameterDefinitionBuilder()
            .withIndexed(document.isIndexed())
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForBytesFixed() {
        BytesFixedParameterDefinitionDocument document = new BytesFixedParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new BytesFixedParameterDefinitionBuilder()
            .withByteLength(document.getByteLength())
            .withIndexed(document.isIndexed())
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForBytes() {
        BytesParameterDefinitionDocument document = new BytesParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new BytesParameterDefinitionBuilder()
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForInt() {
        IntParameterDefinitionDocument document = new IntParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new IntParameterDefinitionBuilder()
            .withBitSize(document.getBitSize())
            .withPosition(document.getPosition())
            .withIndexed(document.isIndexed())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForString() {
        StringParameterDefinitionDocument document = new StringParameterDefinitionDocumentBuilder()
            .build();

        ParameterDefinition expected = new StringParameterDefinitionBuilder()
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForStruct() {
        AddressParameterDefinitionDocument addressParameterDocument = new AddressParameterDefinitionDocumentBuilder().build();
        StringParameterDefinitionDocument stringParameterDocument = new StringParameterDefinitionDocumentBuilder().build();

        StructParameterDefinitionDocument document = new StructParameterDefinitionDocumentBuilder()
            .withParameterDefinitions(Set.of(addressParameterDocument, stringParameterDocument))
            .build();

        ParameterDefinition expectedAddressParameter = new AddressParameterDefinitionBuilder()
            .withIndexed(addressParameterDocument.isIndexed())
            .withPosition(addressParameterDocument.getPosition())
            .build();

        ParameterDefinition expectedStringParameter = new StringParameterDefinitionBuilder()
            .withPosition(stringParameterDocument.getPosition())
            .build();

        ParameterDefinition expected = new StructParameterDefinitionBuilder()
            .withParameterDefinitions(Set.of(expectedAddressParameter, expectedStringParameter))
            .withPosition(document.getPosition())
            .build();

        ParameterDefinition result = ParameterDefinitionDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }
}
