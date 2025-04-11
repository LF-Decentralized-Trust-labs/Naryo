package io.librevents.domain.broadcaster;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinationTest {

    @Test
    void testValidDestination() {
        Destination destination = new Destination("/path/to/resource");
        assertNotNull(destination);
        assertEquals("/path/to/resource", destination.value());
    }

    @Test
    void testNullDestination() {
        assertThrows(NullPointerException.class, () -> new Destination(null));
    }

    @Test
    void testBlankDestination() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Destination("  "));
        assertEquals("value cannot be blank", exception.getMessage());
    }

    @Test
    void testEmptyDestination() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Destination(""));
        assertEquals("value cannot be blank", exception.getMessage());
    }

}
