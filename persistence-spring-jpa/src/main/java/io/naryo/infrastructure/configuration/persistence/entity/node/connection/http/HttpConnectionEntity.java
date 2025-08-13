package io.naryo.infrastructure.configuration.persistence.entity.node.connection.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.NodeConnectionRetryEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("http")
@NoArgsConstructor
public class HttpConnectionEntity extends ConnectionEntity implements HttpNodeConnectionDescriptor {

    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    private @Setter @Nullable @Column(name = "max_idle_connections") Integer maxIdleConnections;
    private @Setter @Nullable @Column(name = "keep_alive_duration") Duration keepAliveDuration;
    private @Setter @Nullable @Column(name = "connection_timeout") Duration connectionTimeout;
    private @Setter @Nullable @Column(name = "read_timeout") Duration readTimeout;

    public HttpConnectionEntity(
            NodeConnectionRetryEntity retry,
            ConnectionEndpointEntity endpoint,
            @Nullable Integer maxIdleConnections,
            @Nullable Duration keepAliveDuration,
            @Nullable Duration connectionTimeout,
            @Nullable Duration readTimeout) {
        super(retry, endpoint);
        this.maxIdleConnections =
                maxIdleConnections != null ? maxIdleConnections : DEFAULT_MAX_IDLE_CONNECTIONS;
        this.keepAliveDuration =
                keepAliveDuration != null ? keepAliveDuration : DEFAULT_KEEP_ALIVE_DURATION;
        this.connectionTimeout =
                connectionTimeout != null ? connectionTimeout : DEFAULT_CONNECTION_TIMEOUT;
        this.readTimeout = readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT;
    }

    @Override
    public Optional<Integer> getMaxIdleConnections() {
        return Optional.ofNullable(this.maxIdleConnections);
    }

    @Override
    public Optional<Duration> getKeepAliveDuration() {
        return Optional.ofNullable(this.keepAliveDuration);
    }

    @Override
    public Optional<Duration> getConnectionTimeout() {
        return Optional.ofNullable(this.connectionTimeout);
    }

    @Override
    public Optional<Duration> getReadTimeout() {
        return Optional.ofNullable(this.readTimeout);
    }
}
