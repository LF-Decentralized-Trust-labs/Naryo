package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UintParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new UintParameter(indexed, position, (Integer) value);
    }

    @Test
    void testConstructor() {
        UintParameter parameter = new UintParameter(true, 1, 123);

        assertEquals(123, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.UINT, parameter.getType());
    }

    @Test
    void testConstructorWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new UintParameter(true, 1, -123));
    }
}
