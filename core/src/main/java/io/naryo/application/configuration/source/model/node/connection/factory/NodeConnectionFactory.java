package io.naryo.application.configuration.source.model.node.connection.factory;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.domain.node.connection.NodeConnection;

public interface NodeConnectionFactory {
    NodeConnection create(NodeConnectionDescriptor descriptor);
}
