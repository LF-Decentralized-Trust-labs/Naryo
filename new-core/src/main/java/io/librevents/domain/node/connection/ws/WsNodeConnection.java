package io.librevents.domain.node.connection.ws;

import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.common.connection.endpoint.Protocol;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.domain.node.connection.RetryConfiguration;

public final class WsNodeConnection extends NodeConnection {
    public WsNodeConnection(ConnectionEndpoint endpoint, RetryConfiguration retryConfiguration) {
        super(NodeConnectionType.WS, endpoint, retryConfiguration);
        if (!(endpoint.getProtocol() == Protocol.WS || endpoint.getProtocol() == Protocol.WSS)) {
            throw new IllegalArgumentException(
                    "Invalid protocol for WS connection: " + endpoint.getProtocol());
        }
    }
}
