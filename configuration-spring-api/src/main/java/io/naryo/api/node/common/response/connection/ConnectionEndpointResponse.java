package io.naryo.api.node.common.response.connection;

import java.util.Map;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;

public record ConnectionEndpointResponse(
        String protocol, String host, int port, String path, Map<String, String> headers) {

    public static ConnectionEndpointResponse fromDomain(ConnectionEndpoint connectionEndpoint) {
        return new ConnectionEndpointResponse(
                connectionEndpoint.getProtocol().getValue(),
                connectionEndpoint.getHost(),
                connectionEndpoint.getPort(),
                connectionEndpoint.getPath(),
                connectionEndpoint.getHeaders());
    }
}
