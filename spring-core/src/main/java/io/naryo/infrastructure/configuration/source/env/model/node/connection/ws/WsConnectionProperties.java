package io.naryo.infrastructure.configuration.source.env.model.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;

public final class WsConnectionProperties extends ConnectionProperties
        implements WsNodeConnectionDescriptor {

    public WsConnectionProperties(
            NodeConnectionRetryDescriptor retry, ConnectionEndpointDescriptor endpoint) {
        super(NodeConnectionType.WS, retry, endpoint);
    }
}
