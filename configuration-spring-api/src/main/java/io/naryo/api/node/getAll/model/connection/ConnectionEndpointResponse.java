package io.naryo.api.node.getAll.model.connection;

import java.util.Map;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.common.connection.endpoint.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Connection endpoint")
public record ConnectionEndpointResponse(
        Protocol protocol, String host, Integer port, String path, Map<String, String> headers) {

    public static ConnectionEndpointResponse fromDomain(ConnectionEndpoint connectionEndpoint) {
        return new ConnectionEndpointResponse(
                connectionEndpoint.getProtocol(),
                connectionEndpoint.getHost(),
                connectionEndpoint.getPort(),
                connectionEndpoint.getPath(),
                connectionEndpoint.getHeaders());
    }
}
