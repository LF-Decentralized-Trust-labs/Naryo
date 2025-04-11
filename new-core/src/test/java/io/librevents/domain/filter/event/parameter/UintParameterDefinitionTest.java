package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UintParameterDefinitionTest {

    @Test
    void testValidUintParameterDefinition() {
        UintParameterDefinition paramDef = new UintParameterDefinition(16, 0, true);
        assertEquals(16, paramDef.getBitSize());
        assertEquals(ParameterType.UINT, paramDef.getType());
        assertEquals(0, paramDef.getPosition());
        assertTrue(paramDef.isIndexed());
    }

    @Test
    void testInvalidBitSizeTooSmall() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UintParameterDefinition(7, 0, true));
        assertEquals("Invalid uint bit size: 7", exception.getMessage());
    }

    @Test
    void testInvalidBitSizeTooLarge() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UintParameterDefinition(257, 0, true));
        assertEquals("Invalid uint bit size: 257", exception.getMessage());
    }

    @Test
    void testInvalidBitSizeNotMultipleOf8() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UintParameterDefinition(10, 0, true));
        assertEquals("Invalid uint bit size: 10", exception.getMessage());
    }

    @Test
    void testInvalidBitSizeNegative() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new UintParameterDefinition(-8, 0, true));
        assertEquals("Invalid uint bit size: -8", exception.getMessage());
    }
}
