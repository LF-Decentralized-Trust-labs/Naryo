package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new BytesParameter(indexed, position, (byte[]) value);
    }

    @Test
    void testConstructor() {
        byte[] value = new byte[] {0x01, 0x02, 0x03};
        BytesParameter parameter = new BytesParameter(true, 1, value);

        assertArrayEquals(value, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.BYTES, parameter.getType());
    }
}
