package io.naryo.api.node.common.response.connection;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

public sealed interface NodeConnectionResponse
        permits HttpNodeConnectionResponse, WsNodeConnectionResponse {

    static NodeConnectionResponse fromDomain(NodeConnection nodeConnection) {
        return switch (nodeConnection) {
            case HttpNodeConnection httpNodeConnection ->
                    HttpNodeConnectionResponse.builder()
                            .type(httpNodeConnection.getType().name())
                            .retryConfiguration(
                                    RetryConfigurationResponse.fromDomain(
                                            httpNodeConnection.getRetryConfiguration()))
                            .connectionEndpoint(
                                    ConnectionEndpointResponse.fromDomain(
                                            httpNodeConnection.getEndpoint()))
                            .connectionTimeout(httpNodeConnection.getConnectionTimeout().value())
                            .maxIdleConnections(httpNodeConnection.getMaxIdleConnections().value())
                            .keepAliveDuration(httpNodeConnection.getKeepAliveDuration().value())
                            .readTimeout(httpNodeConnection.getReadTimeout().value())
                            .build();
            case WsNodeConnection wsNodeConnection ->
                    WsNodeConnectionResponse.builder()
                            .type(wsNodeConnection.getType().name())
                            .retryConfiguration(
                                    RetryConfigurationResponse.fromDomain(
                                            wsNodeConnection.getRetryConfiguration()))
                            .connectionEndpoint(
                                    ConnectionEndpointResponse.fromDomain(
                                            wsNodeConnection.getEndpoint()))
                            .build();
            default -> throw new IllegalStateException("Unexpected value: " + nodeConnection);
        };
    }
}
