package io.naryo.infrastructure.configuration.persistence.document.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ws_connection")
public final class WsConnectionPropertiesDocument extends ConnectionPropertiesDocument
        implements WsNodeConnectionDescriptor {

    public WsConnectionPropertiesDocument(
            NodeConnectionRetryDescriptor retry, ConnectionEndpointDescriptor endpoint) {
        super(NodeConnectionType.WS, retry, endpoint);
    }
}
