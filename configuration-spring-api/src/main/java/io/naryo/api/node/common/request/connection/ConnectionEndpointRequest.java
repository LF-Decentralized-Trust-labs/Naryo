package io.naryo.api.node.common.request.connection;

import java.util.Map;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.common.connection.endpoint.Protocol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConnectionEndpointRequest(
        @NotNull Protocol protocol,
        @NotBlank String host,
        @NotNull Integer port,
        @NotBlank String path,
        Map<String, String> headers) {

    public ConnectionEndpoint toDomain() {
        return new ConnectionEndpoint(protocol, host, port, path, headers);
    }
}
