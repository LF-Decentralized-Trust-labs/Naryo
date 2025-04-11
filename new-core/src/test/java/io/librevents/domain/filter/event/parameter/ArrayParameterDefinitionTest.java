package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayParameterDefinitionTest {

    @Test
    void testArrayParameterDefinition() {
        ArrayParameterDefinition arrayParameterDefinition =
                new ArrayParameterDefinition(0, new BytesParameterDefinition(1), 5);
        assertEquals(ParameterType.ARRAY, arrayParameterDefinition.getType());
        assertEquals(0, arrayParameterDefinition.getPosition());
        assertEquals(5, arrayParameterDefinition.getFixedLength());
        assertTrue(arrayParameterDefinition.getElementType() instanceof BytesParameterDefinition);
    }

    @Test
    void testArrayParameterDefinitionWithNullElementType() {
        assertThrows(
                NullPointerException.class,
                () -> new ArrayParameterDefinition(0, null, 5),
                "elementType must not be null");
    }

    @Test
    void testArrayParameterDefinitionWithNullFixedLength() {
        ArrayParameterDefinition arrayParameterDefinition =
                new ArrayParameterDefinition(0, new BytesParameterDefinition(1), null);
        assertNull(arrayParameterDefinition.getFixedLength());
    }
}
