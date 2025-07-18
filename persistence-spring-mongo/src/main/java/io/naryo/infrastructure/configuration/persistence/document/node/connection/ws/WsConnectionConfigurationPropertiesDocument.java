package io.naryo.infrastructure.configuration.persistence.document.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.NodeConnectionPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ws_connection")
public final class WsConnectionConfigurationPropertiesDocument
        extends NodeConnectionPropertiesDocument implements WsNodeConnectionDescriptor {}
