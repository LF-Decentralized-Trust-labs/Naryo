package io.librevents.domain.event.contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractContractEventParameterTest {

    protected abstract void createParameter(boolean indexed, int position, Object value);

    @Test
    void testNullValue() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    createParameter(true, 1, null);
                });
    }

    @Test
    void testInvalidPosition() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    createParameter(true, -1, null);
                });
    }
}
