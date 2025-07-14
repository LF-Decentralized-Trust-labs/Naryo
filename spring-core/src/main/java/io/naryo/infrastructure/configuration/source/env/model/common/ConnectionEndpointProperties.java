package io.naryo.infrastructure.configuration.source.env.model.common;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class ConnectionEndpointProperties implements ConnectionEndpointDescriptor {

    private @Getter @Setter @NotBlank String url;

    public ConnectionEndpointProperties(String url) {
        this.url = url;
    }

    public ConnectionEndpointProperties() {}
}
