package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.filter.event.ParameterDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayParameterDefinitionTest {

    @Test
    void testArrayParameterDefinition() {
        ParameterDefinition elementType = new StringParameterDefinition();
        ArrayParameterDefinition arrayParameterDefinition =
                new ArrayParameterDefinition(0, elementType, 5);
        assertEquals(ParameterType.ARRAY, arrayParameterDefinition.getType());
        assertEquals(0, arrayParameterDefinition.getPosition());
        assertEquals(5, arrayParameterDefinition.getFixedLength());
        assertEquals(arrayParameterDefinition.getElementType(), elementType);
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
                new ArrayParameterDefinition(0, new BytesParameterDefinition(), null);
        assertNull(arrayParameterDefinition.getFixedLength());
    }
}
