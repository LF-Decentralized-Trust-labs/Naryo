package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesFixedParameterDefinitionTest {

    @Test
    void testValidByteLength() {
        BytesFixedParameterDefinition paramDef = new BytesFixedParameterDefinition(16, 0, true);
        assertEquals(16, paramDef.getByteLength());
        assertEquals(ParameterType.BYTES_FIXED, paramDef.getType());
        assertTrue(paramDef.isIndexed());
    }

    @Test
    void testInvalidByteLengthTooSmall() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new BytesFixedParameterDefinition(0, 0, true));
        assertEquals("Invalid bytes length: 0", exception.getMessage());
    }

    @Test
    void testInvalidByteLengthTooLarge() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new BytesFixedParameterDefinition(33, 0, true));
        assertEquals("Invalid bytes length: 33", exception.getMessage());
    }
}
