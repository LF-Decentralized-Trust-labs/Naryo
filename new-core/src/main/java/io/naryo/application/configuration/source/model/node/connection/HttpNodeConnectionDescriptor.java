package io.naryo.application.configuration.source.model.node.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.NodeConnectionType;

public interface HttpNodeConnectionDescriptor extends NodeConnectionDescriptor {

    @Override
    default NodeConnectionType getType() {
        return NodeConnectionType.HTTP;
    }

    int getMaxIdleConnections();

    Duration getKeepAliveDuration();

    Duration getConnectionTimeout();

    Duration getReadTimeout();

    void setMaxIdleConnections(int maxIdleConnections);

    void setKeepAliveDuration(Duration keepAliveDuration);

    void setConnectionTimeout(Duration connectionTimeout);

    void setReadTimeout(Duration readTimeout);

    @Override
    default NodeConnectionDescriptor merge(NodeConnectionDescriptor other) {
        var connection = NodeConnectionDescriptor.super.merge(other);

        if (other instanceof HttpNodeConnectionDescriptor otherDescriptor) {
            if (this.getMaxIdleConnections() != otherDescriptor.getMaxIdleConnections()) {
                this.setMaxIdleConnections(otherDescriptor.getMaxIdleConnections());
            }

            if (this.getKeepAliveDuration() != otherDescriptor.getKeepAliveDuration()) {
                this.setKeepAliveDuration(otherDescriptor.getKeepAliveDuration());
            }

            if (this.getConnectionTimeout() != otherDescriptor.getConnectionTimeout()) {
                this.setConnectionTimeout(otherDescriptor.getConnectionTimeout());
            }

            if (this.getReadTimeout() != otherDescriptor.getReadTimeout()) {
                this.setReadTimeout(otherDescriptor.getReadTimeout());
            }
        }

        return connection;
    }
}
