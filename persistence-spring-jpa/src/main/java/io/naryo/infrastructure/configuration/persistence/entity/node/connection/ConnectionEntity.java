package io.naryo.infrastructure.configuration.persistence.entity.node.connection;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@Table(name = "connections")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "connection_type", discriminatorType = DiscriminatorType.STRING, length = 50)
@NoArgsConstructor
public abstract class ConnectionEntity implements NodeConnectionDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private java.util.UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "times", column = @Column(name = "retry_times")),
        @AttributeOverride(name = "backoff", column = @Column(name = "retry_backoff"))
    })
    private @Nullable NodeConnectionRetryEntity retry;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "url", column = @Column(name = "endpoint_url", length = 255))
    })
    private @Nullable ConnectionEndpointEntity endpoint;

    public ConnectionEntity(
        @Nullable NodeConnectionRetryEntity retry,
        @Nullable ConnectionEndpointEntity endpoint) {
        this.retry = retry != null ? retry : new NodeConnectionRetryEntity();
        this.endpoint = endpoint;
    }

    @Override
    public Optional<NodeConnectionRetryEntity> getRetry() {
        return Optional.ofNullable(retry);
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.retry = new NodeConnectionRetryEntity(
            valueOrNull(retry.getTimes()),
            valueOrNull(retry.getBackoff())
        );
    }

    @Override
    public Optional<ConnectionEndpointEntity> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.endpoint = new ConnectionEndpointEntity(valueOrNull(endpoint.getUrl()));
    }


}
