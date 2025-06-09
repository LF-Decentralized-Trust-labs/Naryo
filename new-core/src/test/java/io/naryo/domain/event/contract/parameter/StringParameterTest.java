package io.naryo.domain.event.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new StringParameter(indexed, position, (String) value);
    }

    @Test
    void testConstructor() {
        String value = "testString";
        StringParameter parameter = new StringParameter(true, 1, value);

        assertEquals(value, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.STRING, parameter.getType());
    }
}
