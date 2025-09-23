package io.naryo.domain.configuration.store.active;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import org.instancio.Instancio;

public final class HttpStoreConfigurationBuilder
        extends ActiveStoreConfigurationBuilder<
                HttpStoreConfigurationBuilder, HttpStoreConfiguration> {

    private ConnectionEndpoint endpoint;

    @Override
    public HttpStoreConfigurationBuilder self() {
        return this;
    }

    @Override
    public HttpStoreConfiguration build() {
        return new HttpStoreConfiguration(getNodeId(), getFeatures(), getEndpoint());
    }

    public HttpStoreConfigurationBuilder withEndpoint(ConnectionEndpoint endpoint) {
        this.endpoint = endpoint;
        return self();
    }

    public ConnectionEndpoint getEndpoint() {
        return this.endpoint == null ? Instancio.create(ConnectionEndpoint.class) : this.endpoint;
    }
}
