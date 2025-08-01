package io.naryo.application.configuration.source.model.node.connection;

import io.naryo.domain.node.connection.NodeConnectionType;

public interface WsNodeConnectionDescriptor extends NodeConnectionDescriptor {

    @Override
    default NodeConnectionType getType() {
        return NodeConnectionType.WS;
    }

    @Override
    default NodeConnectionDescriptor merge(NodeConnectionDescriptor other) {
        if (!(other instanceof WsNodeConnectionDescriptor otherWsNodeConnection)) {
            return this;
        }

        return NodeConnectionDescriptor.super.merge(otherWsNodeConnection);
    }
}
