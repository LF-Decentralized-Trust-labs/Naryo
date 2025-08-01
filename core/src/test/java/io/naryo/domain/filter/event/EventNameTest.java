package io.naryo.domain.filter.event;

import io.naryo.domain.common.event.EventName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventNameTest {

    @Test
    void testValidEventName() {
        EventName eventName = new EventName("TransferTransactionEvent");
        assertEquals("TransferTransactionEvent", eventName.value());
    }

    @Test
    void testNullEventName() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new EventName(null);
                        });
        assertEquals("Event name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyEventName() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new EventName("");
                        });
        assertEquals("Event name cannot be null or empty", exception.getMessage());
    }
}
