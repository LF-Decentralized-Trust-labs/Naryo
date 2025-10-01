package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.naryo.infrastructure.configuration.persistence.document.common.ConnectionEndpointPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.http.HttpConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ws.WsConnectionPropertiesDocument;
import jakarta.annotation.Nullable;
import org.springframework.data.mongodb.core.mapping.Document;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Document
public abstract class ConnectionPropertiesDocument implements NodeConnectionDescriptor {

    private @Nullable NodeConnectionRetryPropertiesDocument retry;
    private @Nullable ConnectionEndpointPropertiesDocument endpoint;

    public ConnectionPropertiesDocument(
            NodeConnectionRetryPropertiesDocument retry,
            ConnectionEndpointPropertiesDocument endpoint) {
        this.retry = retry;
        this.endpoint = endpoint;
    }

    @Override
    public Optional<NodeConnectionRetryPropertiesDocument> getRetry() {
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

    public static ConnectionPropertiesDocument fromDomain(NodeConnection source) {
        return switch (source) {
            case WsNodeConnection ws ->
                    new WsConnectionPropertiesDocument(
                            new NodeConnectionRetryPropertiesDocument(
                                    ws.getRetryConfiguration().times(),
                                    ws.getRetryConfiguration().backoff()),
                            new ConnectionEndpointPropertiesDocument(ws.getEndpoint().getUrl()));
            case HttpNodeConnection http ->
                    new HttpConnectionPropertiesDocument(
                            new NodeConnectionRetryPropertiesDocument(
                                    http.getRetryConfiguration().times(),
                                    http.getRetryConfiguration().backoff()),
                            new ConnectionEndpointPropertiesDocument(http.getEndpoint().getUrl()),
                            http.getMaxIdleConnections().value(),
                            http.getKeepAliveDuration().value(),
                            http.getConnectionTimeout().value(),
                            http.getReadTimeout().value());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported connection type: " + source.getClass().getSimpleName());
        };
    }
}
