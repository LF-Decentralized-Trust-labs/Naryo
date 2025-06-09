package io.naryo.domain.node.connection.ws;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.common.connection.endpoint.Protocol;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.domain.node.connection.RetryConfiguration;

public final class WsNodeConnection extends NodeConnection {
    public WsNodeConnection(ConnectionEndpoint endpoint, RetryConfiguration retryConfiguration) {
        super(NodeConnectionType.WS, endpoint, retryConfiguration);
        if (!(endpoint.getProtocol() == Protocol.WS || endpoint.getProtocol() == Protocol.WSS)) {
            throw new IllegalArgumentException(
                    "Invalid protocol for WS connection: " + endpoint.getProtocol());
        }
    }
}
