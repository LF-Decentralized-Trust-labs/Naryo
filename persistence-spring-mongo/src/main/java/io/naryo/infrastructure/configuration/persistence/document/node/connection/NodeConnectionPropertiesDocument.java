package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConnectionEndpointPropertiesDocument;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public abstract class NodeConnectionPropertiesDocument implements NodeConnectionDescriptor {

    private @NotNull NodeConnectionType type;
    private RetryPropertiesDocument retry;
    private @Valid @NotNull ConnectionEndpointPropertiesDocument endpoint;

    @Override
    public NodeConnectionRetryDescriptor getRetry() {
        return retry == null ? new RetryPropertiesDocument() : retry;
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.retry = (RetryPropertiesDocument) retry;
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.endpoint = (ConnectionEndpointPropertiesDocument) endpoint;
    }
}
