package io.naryo.domain.configuration.broadcaster.rabbitmq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

    @Test
    void constructor_withNullValue() {
        assertThrows(NullPointerException.class, () -> new Exchange(null));
    }

    @Test
    void constructor_withBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new Exchange(" "));
    }

    @Test
    void constructor_withValidValue() {
        assertDoesNotThrow(() -> new Exchange("test"));
    }
}
