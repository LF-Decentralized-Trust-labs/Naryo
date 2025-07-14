package io.naryo.infrastructure.configuration.source.env.model.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.NodeConnectionRetryProperties;

public final class WsConnectionProperties extends ConnectionProperties
        implements WsNodeConnectionDescriptor {

    public WsConnectionProperties(
            NodeConnectionRetryProperties retry, ConnectionEndpointProperties endpoint) {
        super(NodeConnectionType.WS, retry, endpoint);
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.setEndpoint((ConnectionEndpointProperties) endpoint);
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.setRetry((NodeConnectionRetryProperties) retry);
    }
}
