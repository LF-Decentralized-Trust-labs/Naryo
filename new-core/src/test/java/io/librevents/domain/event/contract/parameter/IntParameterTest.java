package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new IntParameter(indexed, position, (Integer) value);
    }

    @Test
    void testConstructor() {
        IntParameter parameter = new IntParameter(true, 1, 123);

        assertEquals(123, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.INT, parameter.getType());
    }

    @Test
    void testConstructorWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new IntParameter(true, 1, -123));
    }
}
