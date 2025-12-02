package io.naryo.domain.event.contract.parameter;

import java.math.BigInteger;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new IntParameter(indexed, position, (BigInteger) value);
    }

    @Test
    void testConstructor() {
        IntParameter parameter = new IntParameter(true, 1, BigInteger.valueOf(123));

        assertEquals(BigInteger.valueOf(123), parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.INT, parameter.getType());
    }
}
