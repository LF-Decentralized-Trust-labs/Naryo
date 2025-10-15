package io.naryo.api.node.common.request.connection;

import io.naryo.domain.node.connection.NodeConnectionType;

public final class WsNodeConnectionRequest extends NodeConnectionRequest {

    public WsNodeConnectionRequest(
            RetryConfigurationRequest retryConfiguration,
            ConnectionEndpointRequest connectionEndpoint) {
        super(NodeConnectionType.WS, retryConfiguration, connectionEndpoint);
    }
}
