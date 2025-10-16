package io.naryo.api.node.common.connection;


import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.request.connection.ConnectionEndpointRequest;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.connection.RetryConfigurationRequest;
import org.instancio.Instancio;

public abstract class NodeConnectionRequestBuilder<
                T extends NodeConnectionRequestBuilder<T, Y>, Y extends NodeConnectionRequest>
        implements RequestBuilder<T, Y> {

    private RetryConfigurationRequest retryConfiguration;
    private ConnectionEndpointRequest endpoint;

    public T withRetryConfiguration(RetryConfigurationRequest retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
        return self();
    }

    public T withEndpoint(ConnectionEndpointRequest endpoint) {
        this.endpoint = endpoint;
        return self();
    }

    protected RetryConfigurationRequest getRetryConfiguration() {
        return this.retryConfiguration == null
                ? Instancio.create(RetryConfigurationRequest.class)
                : this.retryConfiguration;
    }

    protected ConnectionEndpointRequest getEndpoint() {
        return this.endpoint == null
                ? Instancio.create(ConnectionEndpointRequest.class)
                : this.endpoint;
    }
}
