package io.librevents.domain.node.connection.ws;

import java.time.Duration;

import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.connection.http.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WsNodeConnectionTest {

    @Test
    void testValidHttpNodeConnection() {
        WsNodeConnection connection =
                new WsNodeConnection(
                        new ConnectionEndpoint("wss://example.com"),
                        new RetryConfiguration(3, Duration.ofSeconds(5)));

        assertNotNull(connection);
        assertEquals("wss://example.com:443/", connection.getEndpoint().getUrl());
        assertEquals(3, connection.getRetryConfiguration().times());
        assertEquals(Duration.ofSeconds(5), connection.getRetryConfiguration().backoff());
    }

    @Test
    void testNullEndpoint() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new WsNodeConnection(
                                        null, new RetryConfiguration(3, Duration.ofSeconds(5))));
        assertEquals("Endpoint cannot be null", exception.getMessage());
    }

    @Test
    void testNullRetryConfiguration() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new WsNodeConnection(
                                        new ConnectionEndpoint("ws://example.com"), null));
        assertEquals("RetryConfiguration cannot be null", exception.getMessage());
    }

    @Test
    void testInvalidProtocol() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                new WsNodeConnection(
                                        new ConnectionEndpoint("http://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5))));
        assertEquals("Invalid protocol for WS connection: http", exception.getMessage());
    }
}
