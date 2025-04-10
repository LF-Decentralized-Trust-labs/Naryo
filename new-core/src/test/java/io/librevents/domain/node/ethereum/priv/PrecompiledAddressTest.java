package io.librevents.domain.node.ethereum.priv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class PrecompiledAddressTest {

    @Test
    void testPrecompiledAddressCreation() {
        PrecompiledAddress precompiledAddress = new PrecompiledAddress("0x1234567890abcdef");
        assertEquals("0x1234567890abcdef", precompiledAddress.value());
    }

    @Test
    void testPrecompiledAddressCreationWithNullValue() {
        assertThrows(NullPointerException.class, () -> new PrecompiledAddress(null));
    }

    @Test
    void testPrecompiledAddressCreationWithEmptyValue() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new PrecompiledAddress(""));
        assertEquals("Value must not be empty", exception.getMessage());
    }
}
