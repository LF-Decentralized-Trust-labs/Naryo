package io.naryo.domain.node.connection;

import io.naryo.domain.common.connection.endpoint.Protocol;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

public final class WsNodeConnectionBuilder
        extends NodeConnectionBuilder<WsNodeConnectionBuilder, WsNodeConnection> {
    @Override
    public WsNodeConnectionBuilder self() {
        return this;
    }

    @Override
    public WsNodeConnection build() {
        return new WsNodeConnection(
                getEndpoint().toBuilder().protocol(Protocol.WS).build(), getRetryConfiguration());
    }
}
