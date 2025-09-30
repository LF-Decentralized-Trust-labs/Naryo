package io.naryo.infrastructure.configuration.persistence.entity.node.connection;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.http.HttpConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ws.WsConnectionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@Table(name = "node_connection")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "connection_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class ConnectionEntity implements NodeConnectionDescriptor {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(
            name = "id",
            nullable = false,
            updatable = false) UUID id;

    private @Embedded @Nullable NodeConnectionRetryEntity retry;

    private @Embedded @Nullable ConnectionEndpointEntity endpoint;

    public ConnectionEntity(
            @Nullable NodeConnectionRetryEntity retry,
            @Nullable ConnectionEndpointEntity endpoint) {
        this.retry = retry;
        this.endpoint = endpoint;
    }

    @Override
    public Optional<NodeConnectionRetryEntity> getRetry() {
        return Optional.ofNullable(this.retry);
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.retry =
                new NodeConnectionRetryEntity(
                        valueOrNull(retry.getTimes()), valueOrNull(retry.getBackoff()));
    }

    @Override
    public Optional<ConnectionEndpointEntity> getEndpoint() {
        return Optional.ofNullable(this.endpoint);
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.endpoint = new ConnectionEndpointEntity(valueOrNull(endpoint.getUrl()));
    }

    public UUID getId() {
        return this.id;
    }

    public static ConnectionEntity fromDescriptor(NodeConnectionDescriptor descriptor) {
        return switch (descriptor) {
            case WsNodeConnectionDescriptor ws ->
                    new WsConnectionEntity(
                            valueOrNull(ws.getRetry()), valueOrNull(ws.getEndpoint()));
            case HttpNodeConnectionDescriptor http ->
                    new HttpConnectionEntity(
                            valueOrNull(http.getRetry()),
                            valueOrNull(http.getEndpoint()),
                            valueOrNull(http.getMaxIdleConnections()),
                            valueOrNull(http.getKeepAliveDuration()),
                            valueOrNull(http.getConnectionTimeout()),
                            valueOrNull(http.getReadTimeout()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported connection type: "
                                    + descriptor.getClass().getSimpleName());
        };
    }
}
