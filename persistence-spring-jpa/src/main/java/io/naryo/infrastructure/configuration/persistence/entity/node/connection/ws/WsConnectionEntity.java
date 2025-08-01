package io.naryo.infrastructure.configuration.persistence.entity.node.connection.ws;

import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.NodeConnectionRetryEntity;


public class WsConnectionEntity extends ConnectionEntity implements WsNodeConnectionDescriptor {

    public WsConnectionEntity(
        NodeConnectionRetryEntity retry,
        ConnectionEndpointEntity endpoint) {
        super(retry, endpoint);
    }
}
