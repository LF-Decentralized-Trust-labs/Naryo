package io.librevents.domain.filter.event.parameter;

import java.util.Set;

import io.librevents.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructParameterDefinitionTest {

    @Test
    void testStructParameterDefinition() {
        StructParameterDefinition structParamDef =
                new StructParameterDefinition(1, Set.of(new StringParameterDefinition(1)));
        assertEquals(1, structParamDef.getPosition());
        assertEquals(ParameterType.STRUCT, structParamDef.getType());
        assertNotNull(structParamDef.getParameterDefinitions());
        assertEquals(1, structParamDef.getParameterDefinitions().size());
    }

    @Test
    void testStructParameterDefinitionWithEmptyList() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new StructParameterDefinition(1, Set.of()));
        assertEquals("parameterDefinitions cannot be empty", exception.getMessage());
    }

    @Test
    void testStructParameterDefinitionWithNullList() {
        Exception exception =
                assertThrows(
                        NullPointerException.class, () -> new StructParameterDefinition(1, null));
        assertEquals("parameterDefinitions cannot be null", exception.getMessage());
    }
}
