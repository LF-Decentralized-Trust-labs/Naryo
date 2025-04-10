package io.librevents.domain.node.interaction.block.hedera;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class RetriesPerRequestTest {

    @Test
    void testValidRetriesPerRequest() {
        int validValue = 5;
        RetriesPerRequest retriesPerRequest = new RetriesPerRequest(validValue);
        assertEquals(validValue, retriesPerRequest.value());
    }

    @Test
    void testInvalidRetriesPerRequest() {
        int invalidValue = -1;
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> new RetriesPerRequest(invalidValue));
        assertEquals("Value must be positive", exception.getMessage());
    }
}
