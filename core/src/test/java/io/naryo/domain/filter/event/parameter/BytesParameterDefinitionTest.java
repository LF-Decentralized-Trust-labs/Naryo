package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BytesParameterDefinitionTest {

    @Test
    void testConstructor() {
        BytesParameterDefinition parameterDefinition = new BytesParameterDefinition(1, false);
        assertEquals(ParameterType.BYTES, parameterDefinition.getType());
        assertEquals(1, parameterDefinition.getPosition());
    }
}
