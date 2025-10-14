package io.naryo.api.node.common.response.connection;

import io.naryo.api.node.common.response.connection.http.HttpNodeConnectionResponse;
import io.naryo.api.node.common.response.connection.ws.WsNodeConnectionResponse;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

public abstract class NodeConnectionResponse {

    private final String type;
    private final RetryConfigurationResponse retryConfiguration;
    private final ConnectionEndpointResponse connectionEndpoint;

    public NodeConnectionResponse(
            String type,
            RetryConfigurationResponse retryConfiguration,
            ConnectionEndpointResponse connectionEndpoint) {
        this.type = type;
        this.connectionEndpoint = connectionEndpoint;
        this.retryConfiguration = retryConfiguration;
    }

    public static NodeConnectionResponse fromDomain(NodeConnection nodeConnection) {
        return switch (nodeConnection) {
            case HttpNodeConnection httpNodeConnection ->
                    new HttpNodeConnectionResponse(
                            httpNodeConnection.getType().name(),
                            RetryConfigurationResponse.fromDomain(
                                    httpNodeConnection.getRetryConfiguration()),
                            ConnectionEndpointResponse.fromDomain(httpNodeConnection.getEndpoint()),
                            httpNodeConnection.getMaxIdleConnections().value(),
                            httpNodeConnection.getKeepAliveDuration().value(),
                            httpNodeConnection.getConnectionTimeout().value(),
                            httpNodeConnection.getReadTimeout().value());
            case WsNodeConnection wsNodeConnection ->
                    new WsNodeConnectionResponse(
                            wsNodeConnection.getType().name(),
                            RetryConfigurationResponse.fromDomain(
                                    wsNodeConnection.getRetryConfiguration()),
                            ConnectionEndpointResponse.fromDomain(wsNodeConnection.getEndpoint()));
            default -> throw new IllegalStateException("Unexpected value: " + nodeConnection);
        };
    }
}
