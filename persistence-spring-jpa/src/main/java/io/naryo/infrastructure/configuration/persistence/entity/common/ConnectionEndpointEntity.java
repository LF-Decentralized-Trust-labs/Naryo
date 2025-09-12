package io.naryo.infrastructure.configuration.persistence.entity.common;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Embeddable
@NoArgsConstructor
public class ConnectionEndpointEntity implements ConnectionEndpointDescriptor {

    private @Nullable @Column(name = "url") String url;

    public ConnectionEndpointEntity(String url) {
        this.url = url;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
