package io.naryo.infrastructure.configuration.persistence.document.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConnectionEndpointPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.NodeConnectionRetryPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("ws_connection")
public final class WsConnectionPropertiesDocument extends ConnectionPropertiesDocument
        implements WsNodeConnectionDescriptor {

    public WsConnectionPropertiesDocument(
        NodeConnectionRetryPropertiesDocument retry, ConnectionEndpointPropertiesDocument endpoint) {
        super(NodeConnectionType.WS, retry, endpoint);
    }
}
