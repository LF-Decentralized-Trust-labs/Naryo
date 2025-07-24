package io.naryo.application.configuration.source.model.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.domain.node.connection.NodeConnectionType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface HttpNodeConnectionDescriptor extends NodeConnectionDescriptor {

    Optional<Integer> getMaxIdleConnections();

    Optional<Duration> getKeepAliveDuration();

    Optional<Duration> getConnectionTimeout();

    Optional<Duration> getReadTimeout();

    void setMaxIdleConnections(Integer maxIdleConnections);

    void setKeepAliveDuration(Duration keepAliveDuration);

    void setConnectionTimeout(Duration connectionTimeout);

    void setReadTimeout(Duration readTimeout);

    @Override
    default NodeConnectionType getType() {
        return NodeConnectionType.HTTP;
    }

    @Override
    default NodeConnectionDescriptor merge(NodeConnectionDescriptor other) {
        if (!(other instanceof HttpNodeConnectionDescriptor otherDescriptor)) {
            return this;
        }

        mergeOptionals(
                this::setMaxIdleConnections,
                this.getMaxIdleConnections(),
                otherDescriptor.getMaxIdleConnections());
        mergeOptionals(
                this::setKeepAliveDuration,
                this.getKeepAliveDuration(),
                otherDescriptor.getKeepAliveDuration());
        mergeOptionals(
                this::setConnectionTimeout,
                this.getConnectionTimeout(),
                otherDescriptor.getConnectionTimeout());
        mergeOptionals(
                this::setReadTimeout, this.getReadTimeout(), otherDescriptor.getReadTimeout());

        return NodeConnectionDescriptor.super.merge(other);
    }
}
