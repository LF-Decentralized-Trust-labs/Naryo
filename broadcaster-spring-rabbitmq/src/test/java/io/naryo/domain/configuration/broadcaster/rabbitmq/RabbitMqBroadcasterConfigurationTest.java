package io.naryo.domain.configuration.broadcaster.rabbitmq;

import java.util.UUID;

import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RabbitMqBroadcasterConfigurationTest {

    @Mock Exchange exchange;
    @Mock private BroadcasterCache cache;

    @Test
    void constructor_withNullValues() {
        assertThrows(
                NullPointerException.class,
                () -> new RabbitMqBroadcasterConfiguration(null, cache, exchange));
        assertThrows(
                NullPointerException.class,
                () -> new RabbitMqBroadcasterConfiguration(UUID.randomUUID(), null, exchange));
        assertThrows(
                NullPointerException.class,
                () -> new RabbitMqBroadcasterConfiguration(UUID.randomUUID(), cache, null));
    }

    @Test
    void constructor_withValidValues() {
        assertDoesNotThrow(
                () -> new RabbitMqBroadcasterConfiguration(UUID.randomUUID(), cache, exchange));
    }

    @Test
    void getType() {
        RabbitMqBroadcasterConfiguration config =
                new RabbitMqBroadcasterConfiguration(UUID.randomUUID(), cache, exchange);
        assertDoesNotThrow(config::getType);
    }
}
