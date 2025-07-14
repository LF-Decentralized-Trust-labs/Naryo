package io.naryo.application.configuration.source.model.node.connection;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;

public interface NodeConnectionDescriptor extends MergeableDescriptor<NodeConnectionDescriptor> {

    NodeConnectionType getType();

    ConnectionEndpointDescriptor getEndpoint();

    NodeConnectionRetryDescriptor getRetry();

    void setEndpoint(ConnectionEndpointDescriptor endpoint);

    void setRetry(NodeConnectionRetryDescriptor retry);

    @Override
    default NodeConnectionDescriptor merge(NodeConnectionDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getType().equals(other.getType())) {
            return other;
        }

        if (!this.getEndpoint().equals(other.getEndpoint())) {
            this.setEndpoint(this.getEndpoint().merge(other.getEndpoint()));
        }

        if (this.getRetry() != null && other.getRetry() != null) {
            this.setRetry(this.getRetry().merge(other.getRetry()));
        }

        return this;
    }
}
