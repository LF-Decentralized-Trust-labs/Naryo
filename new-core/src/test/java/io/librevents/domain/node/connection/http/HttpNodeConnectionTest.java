package io.librevents.domain.node.connection.http;

import java.time.Duration;

import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpNodeConnectionTest {

    @Test
    void testValidHttpNodeConnection() {
        HttpNodeConnection connection =
                new HttpNodeConnection(
                        new ConnectionEndpoint("http://example.com"),
                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                        new MaxIdleConnections(5),
                        new KeepAliveDuration(Duration.ofSeconds(30)),
                        new ConnectionTimeout(Duration.ofSeconds(10)),
                        new ReadTimeout(Duration.ofSeconds(1)));

        assertNotNull(connection);
        assertEquals("http://example.com:80/", connection.getEndpoint().getUrl());
        assertEquals(3, connection.getRetryConfiguration().times());
        assertEquals(Duration.ofSeconds(5), connection.getRetryConfiguration().backoff());
        assertEquals(5, connection.getMaxIdleConnections().value());
        assertEquals(Duration.ofSeconds(30), connection.getKeepAliveDuration().value());
        assertEquals(Duration.ofSeconds(10), connection.getConnectionTimeout().value());
        assertEquals(Duration.ofSeconds(1), connection.getReadTimeout().value());
    }

    @Test
    void testNullEndpoint() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        null,
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        new MaxIdleConnections(5),
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("Endpoint cannot be null", exception.getMessage());
    }

    @Test
    void testNullRetryConfiguration() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("http://example.com"),
                                        null,
                                        new MaxIdleConnections(5),
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("RetryConfiguration cannot be null", exception.getMessage());
    }

    @Test
    void testNullMaxIdleConnections() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("http://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        null,
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("maxIdleConnections cannot be null", exception.getMessage());
    }

    @Test
    void testNullKeepAliveDuration() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("http://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        new MaxIdleConnections(5),
                                        null,
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("keepAliveDuration cannot be null", exception.getMessage());
    }

    @Test
    void testNullConnectionTimeout() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("https://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        new MaxIdleConnections(5),
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        null,
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("connectionTimeout cannot be null", exception.getMessage());
    }

    @Test
    void testNullReadTimeout() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("http://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        new MaxIdleConnections(5),
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        null));
        assertEquals("readTimeout cannot be null", exception.getMessage());
    }

    @Test
    void testInvalidProtocol() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                new HttpNodeConnection(
                                        new ConnectionEndpoint("ws://example.com"),
                                        new RetryConfiguration(3, Duration.ofSeconds(5)),
                                        new MaxIdleConnections(5),
                                        new KeepAliveDuration(Duration.ofSeconds(30)),
                                        new ConnectionTimeout(Duration.ofSeconds(10)),
                                        new ReadTimeout(Duration.ofSeconds(1))));
        assertEquals("Invalid protocol for HTTP connection: ws", exception.getMessage());
    }
}
