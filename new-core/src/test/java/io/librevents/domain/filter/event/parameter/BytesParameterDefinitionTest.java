package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesParameterDefinitionTest {

    @Test
    void testConstructor() {
        BytesParameterDefinition parameterDefinition = new BytesParameterDefinition(1);
        assertEquals(ParameterType.BYTES, parameterDefinition.getType());
        assertEquals(1, parameterDefinition.getPosition());
    }
}
