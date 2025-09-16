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
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
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
