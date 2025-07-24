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
        if (!(other instanceof HttpNodeConnectionDescriptor otherHttpNodeConnection)) {
            return this;
        }

        mergeOptionals(
                this::setMaxIdleConnections,
                this.getMaxIdleConnections(),
                otherHttpNodeConnection.getMaxIdleConnections());
        mergeOptionals(
                this::setKeepAliveDuration,
                this.getKeepAliveDuration(),
                otherHttpNodeConnection.getKeepAliveDuration());
        mergeOptionals(
                this::setConnectionTimeout,
                this.getConnectionTimeout(),
                otherHttpNodeConnection.getConnectionTimeout());
        mergeOptionals(
                this::setReadTimeout,
                this.getReadTimeout(),
                otherHttpNodeConnection.getReadTimeout());

        return NodeConnectionDescriptor.super.merge(otherHttpNodeConnection);
    }
}
