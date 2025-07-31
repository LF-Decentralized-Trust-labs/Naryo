package io.naryo.domain.node.ethereum.priv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class GroupIdTest {

    @Test
    void testGroupIdCreation() {
        GroupId groupId = new GroupId("group1");
        assertEquals("group1", groupId.value());
    }

    @Test
    void testGroupIdCreationWithNullValue() {
        assertThrows(NullPointerException.class, () -> new GroupId(null));
    }

    @Test
    void testGroupIdCreationWithEmptyValue() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new GroupId(""));
        assertEquals("Value must not be empty", exception.getMessage());
    }
}
