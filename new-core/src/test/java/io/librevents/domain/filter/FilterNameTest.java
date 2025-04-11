package io.librevents.domain.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class FilterNameTest {

    @Test
    void testValidFilterName() {
        FilterName filterName = new FilterName("ValidFilterName");
        assertEquals("ValidFilterName", filterName.value());
    }

    @Test
    void testNullFilterName() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new FilterName(null);
                        });
        assertEquals("Filter name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyFilterName() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new FilterName("");
                        });
        assertEquals("Filter name cannot be null or empty", exception.getMessage());
    }
}
