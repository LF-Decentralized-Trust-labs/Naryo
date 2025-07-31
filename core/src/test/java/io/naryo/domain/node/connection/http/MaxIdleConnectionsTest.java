package io.naryo.domain.node.connection.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaxIdleConnectionsTest {

    @Test
    void testValidMaxIdleConnections() {
        MaxIdleConnections maxIdleConnections = new MaxIdleConnections(5);
        assertEquals(5, maxIdleConnections.value());
    }

    @Test
    void testNegativeMaxIdleConnections() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new MaxIdleConnections(-1));
        assertEquals("Must be >= 0", exception.getMessage());
    }

    @Test
    void testZeroMaxIdleConnections() {
        MaxIdleConnections maxIdleConnections = new MaxIdleConnections(0);
        assertEquals(0, maxIdleConnections.value());
    }
}
