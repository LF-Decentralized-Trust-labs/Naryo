package io.naryo.infrastructure.configuration.persistence.document.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ConnectionEndpointPropertiesDocument {
    private @NotBlank String url;

    public ConnectionEndpointPropertiesDocument() {
    }

}
