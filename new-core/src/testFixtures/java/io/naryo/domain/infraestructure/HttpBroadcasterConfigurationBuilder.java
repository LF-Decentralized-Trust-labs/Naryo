package io.naryo.domain.infraestructure;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.BroadcasterConfigurationBuilder;
import io.naryo.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import org.instancio.Instancio;

public class HttpBroadcasterConfigurationBuilder
        extends BroadcasterConfigurationBuilder<
                HttpBroadcasterConfigurationBuilder, HttpBroadcasterConfiguration> {

    private ConnectionEndpoint endpoint;

    @Override
    public HttpBroadcasterConfigurationBuilder self() {
        return this;
    }

    @Override
    public HttpBroadcasterConfiguration build() {
        return new HttpBroadcasterConfiguration(getId(), getCache(), getEndpoint());
    }

    public HttpBroadcasterConfigurationBuilder withEndpoint(ConnectionEndpoint endpoint) {
        this.endpoint = endpoint;
        return self();
    }

    protected ConnectionEndpoint getEndpoint() {
        return this.endpoint == null ? Instancio.create(ConnectionEndpoint.class) : this.endpoint;
    }
}
