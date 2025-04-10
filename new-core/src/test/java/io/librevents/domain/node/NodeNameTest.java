package io.librevents.domain.node;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class NodeNameTest {

    @Test
    void testNodeNameCreation() {
        NodeName nodeName = new NodeName("Node1");
        assertEquals("Node1", nodeName.value());
    }

    @Test
    void testNodeNameCreationWithNullValue() {
        assertThrows(NullPointerException.class, () -> new NodeName(null));
    }

    @Test
    void testNodeNameCreationWithEmptyValue() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new NodeName(""));
        assertEquals("Value cannot be empty", exception.getMessage());
    }
}
