package io.librevents.domain.node.interaction.block.hedera;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class LimitPerRequestTest {

    @Test
    void testValidLimitPerRequest() {
        LimitPerRequest limit = new LimitPerRequest(10);
        assertEquals(10, limit.value());
    }

    @Test
    void testInvalidLimitPerRequest() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new LimitPerRequest(-5);
                        });
        assertEquals("Value must be positive", exception.getMessage());
    }
}
