package io.librevents.domain.filter;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractFilterTest {

    protected static final UUID DEFAULT_ID = UUID.randomUUID();
    protected static final String DEFAULT_NAME = "Test";

    protected abstract Filter createFilter(UUID id, FilterName name, UUID nodeId);

    @Test
    void testNullId() {
        assertThrows(
                NullPointerException.class,
                () -> createFilter(null, new FilterName(DEFAULT_NAME), DEFAULT_ID));
    }

    @Test
    void testNullName() {
        assertThrows(NullPointerException.class, () -> createFilter(DEFAULT_ID, null, DEFAULT_ID));
    }

    @Test
    void testNullNodeId() {
        assertThrows(
                NullPointerException.class,
                () -> createFilter(DEFAULT_ID, new FilterName(DEFAULT_NAME), null));
    }
}
