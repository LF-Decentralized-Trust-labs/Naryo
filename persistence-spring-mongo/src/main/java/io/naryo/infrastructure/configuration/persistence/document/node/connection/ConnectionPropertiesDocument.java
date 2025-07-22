package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConnectionEndpointPropertiesDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Document
public abstract class ConnectionPropertiesDocument implements NodeConnectionDescriptor {

    private final @Getter @NotNull NodeConnectionType type;
    private @Nullable NodeConnectionRetryDescriptor retry;
    private @Nullable ConnectionEndpointDescriptor endpoint;

    public ConnectionPropertiesDocument(
            NodeConnectionType type,
            NodeConnectionRetryDescriptor retry,
            ConnectionEndpointDescriptor endpoint) {
        this.type = type;
        this.retry = retry;
        this.endpoint = endpoint;
    }

    @Override
    public Optional<NodeConnectionRetryDescriptor> getRetry() {
        return Optional.ofNullable(retry);
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.retry =
                new NodeConnectionRetryPropertiesDocument(
                        valueOrNull(retry.getTimes()), valueOrNull(retry.getBackoff()));
    }

    @Override
    public Optional<ConnectionEndpointDescriptor> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.endpoint = new ConnectionEndpointPropertiesDocument(valueOrNull(endpoint.getUrl()));
    }
}
