package io.naryo.infrastructure.broadcaster.http.configuration;

import java.util.UUID;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HttpBroadcasterConfigurationTest {

    @Mock private ConnectionEndpoint endpoint;
    @Mock private BroadcasterCache cache;

    @Test
    void constructor_withNullValues() {
        assertThrows(
                NullPointerException.class,
                () -> new HttpBroadcasterConfiguration(null, cache, endpoint));
        assertThrows(
                NullPointerException.class,
                () -> new HttpBroadcasterConfiguration(UUID.randomUUID(), null, endpoint));
        assertThrows(
                NullPointerException.class,
                () -> new HttpBroadcasterConfiguration(UUID.randomUUID(), cache, null));
    }

    @Test
    void constructor_withValidValues() {
        assertDoesNotThrow(
                () -> new HttpBroadcasterConfiguration(UUID.randomUUID(), cache, endpoint));
    }

    @Test
    void getType() {
        HttpBroadcasterConfiguration config =
                new HttpBroadcasterConfiguration(UUID.randomUUID(), cache, endpoint);
        assertDoesNotThrow(config::getType);
    }
}
