package io.naryo.domain.configuration.broadcaster.rabbitmq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoutingKeyTest {

    @Test
    void constructor_withNullValue() {
        assertThrows(NullPointerException.class, () -> new RoutingKey(null));
    }

    @Test
    void constructor_withBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new RoutingKey(" "));
    }

    @Test
    void constructor_withNewline_shouldFail() {
        assertThrows(IllegalArgumentException.class, () -> new RoutingKey("order.created\n"));
    }

    @Test
    void constructor_withExcessiveLength_shouldFail() {
        String tooLong = "a".repeat(256);
        assertThrows(IllegalArgumentException.class, () -> new RoutingKey(tooLong));
    }

    @Test
    void constructor_withIllegalChars_shouldFail() {
        assertThrows(IllegalArgumentException.class, () -> new RoutingKey("ABCdef123!@#"));
    }

    @Test
    void constructor_withValidValue() {
        assertDoesNotThrow(() -> new RoutingKey("test.ok_value-123"));
    }
}
