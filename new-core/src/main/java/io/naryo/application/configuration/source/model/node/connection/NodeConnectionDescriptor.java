package io.naryo.application.configuration.source.model.node.connection;

import java.util.Optional;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;

import static io.naryo.application.common.util.MergeUtil.mergeDescriptors;

public interface NodeConnectionDescriptor extends MergeableDescriptor<NodeConnectionDescriptor> {

    NodeConnectionType getType();

    <T extends ConnectionEndpointDescriptor> Optional<T> getEndpoint();

    <T extends NodeConnectionRetryDescriptor> Optional<T> getRetry();

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

        mergeDescriptors(this::setEndpoint, this.getEndpoint(), other.getEndpoint());
        mergeDescriptors(this::setRetry, this.getRetry(), other.getRetry());

        return this;
    }
}
