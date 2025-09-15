package io.naryo.infrastructure.configuration.persistence.entity.node.connection;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
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
}
