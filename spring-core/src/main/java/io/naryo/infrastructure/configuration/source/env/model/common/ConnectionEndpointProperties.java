package io.naryo.infrastructure.configuration.source.env.model.common;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;

public final class ConnectionEndpointProperties implements ConnectionEndpointDescriptor {

    private @Setter @Nullable String url;

    public ConnectionEndpointProperties(String url) {
        this.url = url;
    }

    public ConnectionEndpointProperties() {}

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
