package io.librevents.domain.node.connection.endpoint;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionEndpointTest {

    @Test
    void testConnectionEndpointCreation() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com");
        assertEquals("http://example.com:80/", endpoint.getUrl());
    }

    @Test
    void testConnectionEndpointCreationWithNullUrl() {
        assertThrows(NullPointerException.class, () -> new ConnectionEndpoint(null));
    }

    @Test
    void testConnectionEndpointCreationWithEmptyUrl() {
        assertThrows(NullPointerException.class, () -> new ConnectionEndpoint(""));
    }

    @Test
    void testConnectionEndpointCreationWithInvalidUrl() {
        assertThrows(NullPointerException.class, () -> new ConnectionEndpoint("invalid-url"));
    }

    @Test
    void testConnectionEndpointCreationWithInvalidProtocol() {
        assertThrows(
                IllegalArgumentException.class, () -> new ConnectionEndpoint("ftp://example.com"));
    }

    @Test
    void testConnectionEndpointCreationWithDefaultPort() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com");
        assertEquals(80, endpoint.getPort());
    }

    @Test
    void testConnectionEndpointCreationWithHttpsDefaultPort() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("https://example.com");
        assertEquals(443, endpoint.getPort());
    }

    @Test
    void testConnectionEndpointCreationWithCustomPort() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com:8080");
        assertEquals(8080, endpoint.getPort());
    }

    @Test
    void testConnectionEndpointCreationWithPath() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com/path/to/resource");
        assertEquals("path/to/resource", endpoint.getPath());
    }

    @Test
    void testConnectionEndpointCreationWithEmptyPath() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com/");
        assertEquals("", endpoint.getPath());
    }

    @Test
    void testConnectionEndpointCreationWithoutHeaders() {
        ConnectionEndpoint endpoint = new ConnectionEndpoint("http://example.com");
        assertEquals(0, endpoint.getHeaders().size());
    }

    @Test
    void testConnectionEndpointCreationWithHeaders() {
        ConnectionEndpoint endpoint =
                new ConnectionEndpoint("http://example.com", Map.of("Header1", "Value1"));
        assertEquals(1, endpoint.getHeaders().size());
    }
}
