package io.naryo.domain.node.connection;

import io.naryo.domain.common.connection.endpoint.Protocol;
import io.naryo.domain.node.connection.http.*;
import org.instancio.Instancio;

public final class HttpNodeConnectionBuilder
        extends NodeConnectionBuilder<HttpNodeConnectionBuilder, HttpNodeConnection> {

    private MaxIdleConnections maxIdleConnections;
    private KeepAliveDuration keepAliveDuration;
    private ConnectionTimeout connectionTimeout;
    private ReadTimeout readTimeout;

    @Override
    public HttpNodeConnectionBuilder self() {
        return this;
    }

    @Override
    public HttpNodeConnection build() {
        return new HttpNodeConnection(
                getEndpoint().toBuilder().protocol(Protocol.HTTP).build(),
                getRetryConfiguration(),
                getMaxIdleConnections(),
                getKeepAliveDuration(),
                getConnectionTimeout(),
                getReadTimeout());
    }

    public HttpNodeConnectionBuilder withMaxIdleConnections(MaxIdleConnections maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
        return self();
    }

    public HttpNodeConnectionBuilder withKeepAliveDuration(KeepAliveDuration keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
        return self();
    }

    public HttpNodeConnectionBuilder withConnectionTimeout(ConnectionTimeout connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return self();
    }

    public HttpNodeConnectionBuilder withReadTimeout(ReadTimeout readTimeout) {
        this.readTimeout = readTimeout;
        return self();
    }

    public MaxIdleConnections getMaxIdleConnections() {
        return this.maxIdleConnections == null
                ? Instancio.create(MaxIdleConnections.class)
                : this.maxIdleConnections;
    }

    public KeepAliveDuration getKeepAliveDuration() {
        return this.keepAliveDuration == null
                ? Instancio.create(KeepAliveDuration.class)
                : this.keepAliveDuration;
    }

    public ConnectionTimeout getConnectionTimeout() {
        return this.connectionTimeout == null
                ? Instancio.create(ConnectionTimeout.class)
                : this.connectionTimeout;
    }

    public ReadTimeout getReadTimeout() {
        return this.readTimeout == null ? Instancio.create(ReadTimeout.class) : this.readTimeout;
    }
}
