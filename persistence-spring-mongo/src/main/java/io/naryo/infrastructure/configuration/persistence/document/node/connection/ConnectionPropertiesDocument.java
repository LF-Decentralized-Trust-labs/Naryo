package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConnectionEndpointPropertiesDocument;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public abstract class ConnectionPropertiesDocument {
    private @NotNull NodeConnectionType type;
    private RetryPropertiesDocument retry;
    private @Valid @NotNull ConnectionEndpointPropertiesDocument endpoint;

    public RetryPropertiesDocument getRetry() {
        return retry == null ? new RetryPropertiesDocument() : retry;
    }
}
