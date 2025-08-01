package io.naryo.infrastructure.broadcaster.http.producer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfiguration;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class HttpBroadcasterProducerTest {

    @Mock private OkHttpClient httpClient;
    @Mock private ObjectMapper objectMapper;

    @Test
    void testConstructor_withNullValues() {
        assertThrows(
                NullPointerException.class, () -> new HttpBroadcasterProducer(null, objectMapper));
        assertThrows(
                NullPointerException.class, () -> new HttpBroadcasterProducer(httpClient, null));
    }

    @Test
    void testSupports() {
        HttpBroadcasterProducer producer = new HttpBroadcasterProducer(httpClient, objectMapper);
        assertTrue(producer.supports(() -> "http"));
        assertFalse(producer.supports(() -> "wss"));
    }

    @Test
    void testProduce_withInvalidUrl() {
        Broadcaster broadcaster = mock();
        HttpBroadcasterConfiguration configuration = mock();
        ConnectionEndpoint endpoint = mock();
        BroadcasterTarget target = mock();

        doReturn(endpoint).when(configuration).getEndpoint();
        doReturn("invalid-url").when(endpoint).getUrl();
        doReturn(target).when(broadcaster).getTarget();
        doReturn(new Destination("test")).when(target).getDestination();

        HttpBroadcasterProducer producer = new HttpBroadcasterProducer(httpClient, objectMapper);
        assertThrows(
                IllegalArgumentException.class,
                () -> producer.produce(broadcaster, configuration, mock()));
    }

    @Test
    void testProduce() throws IOException {
        Broadcaster broadcaster = mock();
        HttpBroadcasterConfiguration configuration = mock();
        ConnectionEndpoint endpoint = mock();
        BroadcasterTarget target = mock();
        Call call = mock();

        doReturn(endpoint).when(configuration).getEndpoint();
        doReturn(Map.of()).when(endpoint).getHeaders();
        doReturn("http://localhost:8080").when(endpoint).getUrl();
        doReturn(target).when(broadcaster).getTarget();
        doReturn(new Destination("test")).when(target).getDestination();
        doReturn("").when(objectMapper).writeValueAsString(any());
        doReturn(call).when(httpClient).newCall(any());
        doReturn(null).when(call).execute();

        HttpBroadcasterProducer producer = new HttpBroadcasterProducer(httpClient, objectMapper);
        assertDoesNotThrow(() -> producer.produce(broadcaster, configuration, mock()));
    }
}
