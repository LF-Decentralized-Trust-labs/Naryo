package io.naryo.api.node.common.response.connection.ws;

import io.naryo.api.node.common.response.connection.ConnectionEndpointResponse;
import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.connection.RetryConfigurationResponse;

public final class WsNodeConnectionResponse extends NodeConnectionResponse {

    public WsNodeConnectionResponse(
            String type,
            RetryConfigurationResponse retryConfiguration,
            ConnectionEndpointResponse connectionEndpoint) {
        super(type, retryConfiguration, connectionEndpoint);
    }
}
