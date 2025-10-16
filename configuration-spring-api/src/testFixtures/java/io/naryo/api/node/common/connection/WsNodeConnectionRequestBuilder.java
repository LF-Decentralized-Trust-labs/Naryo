package io.naryo.api.node.common.connection;

import io.naryo.api.node.common.request.connection.WsNodeConnectionRequest;
import io.naryo.domain.common.connection.endpoint.Protocol;

public final class WsNodeConnectionRequestBuilder
        extends NodeConnectionRequestBuilder<
                WsNodeConnectionRequestBuilder, WsNodeConnectionRequest> {
    @Override
    public WsNodeConnectionRequestBuilder self() {
        return this;
    }

    @Override
    public WsNodeConnectionRequest build() {
        return new WsNodeConnectionRequest(
                getEndpoint().toBuilder().protocol(Protocol.WS).build(), getRetryConfiguration());
    }
}
