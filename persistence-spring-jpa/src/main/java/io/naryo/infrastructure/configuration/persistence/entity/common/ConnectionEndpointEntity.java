package io.naryo.infrastructure.configuration.persistence.entity.common;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Setter;

import java.util.Optional;

public class ConnectionEndpointEntity implements ConnectionEndpointDescriptor {

    private @Setter @Nullable @Column(name = "url") String url;

    public ConnectionEndpointEntity(String url) {
        this.url = url;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
