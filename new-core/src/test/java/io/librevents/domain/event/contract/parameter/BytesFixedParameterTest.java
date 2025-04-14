package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesFixedParameterTest extends AbstractContractEventParameterTest {

    @Override
    protected void createParameter(boolean indexed, int position, Object value) {
        new BytesFixedParameter(indexed, position, (byte[]) value, 32);
    }

    @Test
    void testConstructor() {
        byte[] value = new byte[32];
        BytesFixedParameter parameter = new BytesFixedParameter(true, 1, value, 32);

        assertArrayEquals(value, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(32, parameter.getByteLength());
    }

    @Test
    void testConstructorWithInvalidLength() {
        byte[] value = new byte[31];
        assertThrows(IllegalArgumentException.class, () -> new BytesFixedParameter(true, 1, value, 32));
    }

    @Test
    void testConstructorWithInvalidLength2() {
        byte[] value = new byte[33];
        assertThrows(IllegalArgumentException.class, () -> new BytesFixedParameter(true, 1, value, 33));
    }

    @Test
    void testConstructorWithInvalidLength3() {
        byte[] value = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> new BytesFixedParameter(true, 1, value, 0));
    }

}
