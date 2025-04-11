package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntParameterDefinitionTest {

    @Test
    void testValidBitSize() {
        IntParameterDefinition param = new IntParameterDefinition(16, 0, true);
        assertEquals(16, param.getBitSize());
        assertEquals(ParameterType.INT, param.getType());
        assertTrue(param.isIndexed());
    }

    @Test
    void testInvalidBitSizeTooSmall() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new IntParameterDefinition(7, 0, true));
        assertEquals("Invalid int bit size: 7", exception.getMessage());
    }

    @Test
    void testInvalidBitSizeTooLarge() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new IntParameterDefinition(257, 0, true));
        assertEquals("Invalid int bit size: 257", exception.getMessage());
    }

    @Test
    void testInvalidBitSizeNotMultipleOf8() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new IntParameterDefinition(10, 0, true));
        assertEquals("Invalid int bit size: 10", exception.getMessage());
    }
}
