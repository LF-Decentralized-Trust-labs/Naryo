package io.librevents.infrastructure.broadcaster.http.configuration;

import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
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
                NullPointerException.class, () -> new HttpBroadcasterConfiguration(cache, null));
    }

    @Test
    void constructor_withValidValues() {
        assertDoesNotThrow(() -> new HttpBroadcasterConfiguration(cache, endpoint));
    }

    @Test
    void getType() {
        HttpBroadcasterConfiguration config = new HttpBroadcasterConfiguration(cache, endpoint);
        assertDoesNotThrow(config::getType);
    }
}
