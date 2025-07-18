package io.naryo.infrastructure.configuration.persistence.document.common;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionEndpointPropertiesDocument implements ConnectionEndpointDescriptor {
    private @NotBlank String url;
}
