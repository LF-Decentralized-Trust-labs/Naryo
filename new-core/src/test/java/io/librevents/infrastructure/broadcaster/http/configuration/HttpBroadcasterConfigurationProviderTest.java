package io.librevents.infrastructure.broadcaster.http.configuration;

import java.util.Map;

import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class HttpBroadcasterConfigurationProviderTest {

    @Mock private ConnectionEndpoint endpoint;
    @Mock private BroadcasterCache cache;

    @Test
    void testConstructor() {
        assertDoesNotThrow(
                () ->
                        new HttpBroadcasterConfigurationProvider()
                                .create(Map.of("cache", cache, "endpoint", endpoint)));
    }

    @Test
    void testSupports() {
        HttpBroadcasterConfigurationProvider provider = new HttpBroadcasterConfigurationProvider();
        assertDoesNotThrow(() -> provider.supports(() -> "http"));
        assertDoesNotThrow(() -> provider.supports(() -> "wss"));
    }
}
