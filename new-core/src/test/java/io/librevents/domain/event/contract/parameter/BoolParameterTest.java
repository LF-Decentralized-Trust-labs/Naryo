package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new BoolParameter(indexed, position, (Boolean) value);
    }

    @Test
    void testBoolContractEventParameter() {
        BoolParameter parameter = new BoolParameter(true, 1, true);
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertTrue(parameter.getValue());
        assertEquals(ParameterType.BOOL, parameter.getType());
    }
}
