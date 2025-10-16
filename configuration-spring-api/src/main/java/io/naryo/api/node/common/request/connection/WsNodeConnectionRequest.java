package io.naryo.api.node.common.request.connection;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

public final class WsNodeConnectionRequest extends NodeConnectionRequest {

    public WsNodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration) {
        super(NodeConnectionType.WS, connectionEndpoint, retryConfiguration);
    }

    @Override
    public NodeConnection toDomain() {
        return new WsNodeConnection(
                this.connectionEndpoint.toDomain(), this.retryConfiguration.toDomain());
    }
}
