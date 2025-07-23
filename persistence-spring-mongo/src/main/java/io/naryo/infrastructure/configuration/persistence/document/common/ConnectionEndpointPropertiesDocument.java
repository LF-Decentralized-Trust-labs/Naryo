package io.naryo.infrastructure.configuration.persistence.document.common;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("connection_endpoint")
public final class ConnectionEndpointPropertiesDocument implements ConnectionEndpointDescriptor {

    private @Setter @Nullable String url;

    public ConnectionEndpointPropertiesDocument(String url) {
        this.url = url;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
