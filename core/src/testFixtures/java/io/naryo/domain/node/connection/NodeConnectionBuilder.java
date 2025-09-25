package io.naryo.domain.node.connection;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import org.instancio.Instancio;

public abstract class NodeConnectionBuilder<
                T extends NodeConnectionBuilder<T, Y>, Y extends NodeConnection>
        implements DomainBuilder<T, Y> {

    private RetryConfiguration retryConfiguration;
    private ConnectionEndpoint endpoint;

    public T withRetryConfiguration(RetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
        return self();
    }

    public T withEndpoint(ConnectionEndpoint endpoint) {
        this.endpoint = endpoint;
        return self();
    }

    protected RetryConfiguration getRetryConfiguration() {
        return this.retryConfiguration == null
                ? Instancio.create(RetryConfiguration.class)
                : this.retryConfiguration;
    }

    protected ConnectionEndpoint getEndpoint() {
        return this.endpoint == null ? Instancio.create(ConnectionEndpoint.class) : this.endpoint;
    }
}
