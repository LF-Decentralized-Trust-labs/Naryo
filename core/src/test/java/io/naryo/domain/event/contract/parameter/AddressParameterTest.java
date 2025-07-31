package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressParameterTest extends AbstractContractEventParameterTest {

    @Test
    void testConstructor() {
        AddressParameter parameter = new AddressParameter(true, 1, "0x1234567890abcdef");

        assertEquals("0x1234567890abcdef", parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.ADDRESS, parameter.getType());
    }

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new AddressParameter(indexed, position, (String) value);
    }
}
