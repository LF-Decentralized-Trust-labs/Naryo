package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolParameterDefinitionTest {

    @Test
    void testBoolParameterDefinition() {
        BoolParameterDefinition boolParameterDefinition = new BoolParameterDefinition(1, true);

        assertEquals(ParameterType.BOOL, boolParameterDefinition.getType());
        assertEquals(1, boolParameterDefinition.getPosition());
        assertTrue(boolParameterDefinition.isIndexed());
    }

    @Test
    void testNegativePosition() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new BoolParameterDefinition(-1, true));
        assertEquals("Position cannot be negative", exception.getMessage());
    }
}
