package io.naryo.api.node.common.connection;

import java.time.Duration;

import io.naryo.api.node.common.request.connection.HttpNodeConnectionRequest;
import io.naryo.domain.common.connection.endpoint.Protocol;
import org.instancio.Instancio;

public final class HttpNodeConnectionRequestBuilder
        extends NodeConnectionRequestBuilder<
                HttpNodeConnectionRequestBuilder, HttpNodeConnectionRequest> {

    private Integer maxIdleConnections;
    private Duration keepAliveDuration;
    private Duration connectionTimeout;
    private Duration readTimeout;

    @Override
    public HttpNodeConnectionRequestBuilder self() {
        return this;
    }

    @Override
    public HttpNodeConnectionRequest build() {
        return new HttpNodeConnectionRequest(
                getEndpoint().toBuilder().protocol(Protocol.HTTP).build(),
                getRetryConfiguration(),
                getMaxIdleConnections(),
                getKeepAliveDuration(),
                getConnectionTimeout(),
                getReadTimeout());
    }

    public HttpNodeConnectionRequestBuilder withMaxIdleConnections(Integer maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
        return self();
    }

    public HttpNodeConnectionRequestBuilder withKeepAliveDuration(Duration keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
        return self();
    }

    public HttpNodeConnectionRequestBuilder withConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return self();
    }

    public HttpNodeConnectionRequestBuilder withReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return self();
    }

    public Integer getMaxIdleConnections() {
        return this.maxIdleConnections == null
                ? Instancio.create(Integer.class)
                : this.maxIdleConnections;
    }

    public Duration getKeepAliveDuration() {
        return this.keepAliveDuration == null
                ? Instancio.create(Duration.class)
                : this.keepAliveDuration;
    }

    public Duration getConnectionTimeout() {
        return this.connectionTimeout == null
                ? Instancio.create(Duration.class)
                : this.connectionTimeout;
    }

    public Duration getReadTimeout() {
        return this.readTimeout == null ? Instancio.create(Duration.class) : this.readTimeout;
    }
}
