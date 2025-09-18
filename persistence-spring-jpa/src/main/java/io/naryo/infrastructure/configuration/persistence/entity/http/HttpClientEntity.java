package io.naryo.infrastructure.configuration.persistence.entity.http;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@Table(name = "http_client")
@NoArgsConstructor
public final class HttpClientEntity implements HttpClientDescriptor {

    private @Column(name = "id") @Id UUID id;

    private @Column(name = "max_idle_connections") Integer maxIdleConnections;

    private @Column(name = "keep_alive_duration") Duration keepAliveDuration;

    private @Column(name = "connect_timeout") Duration connectTimeout;

    private @Column(name = "read_timeout") Duration readTimeout;

    private @Column(name = "write_timeout") Duration writeTimeout;

    private @Column(name = "call_timeout") Duration callTimeout;

    private @Column(name = "ping_interval") Duration pingInterval;

    private @Column(name = "retry_on_connection_failure") Boolean retryOnConnectionFailure;

    @Override
    public Optional<Integer> getMaxIdleConnections() {
        return Optional.ofNullable(maxIdleConnections);
    }

    @Override
    public Optional<Duration> getKeepAliveDuration() {
        return Optional.ofNullable(this.keepAliveDuration);
    }

    @Override
    public Optional<Duration> getConnectTimeout() {
        return Optional.ofNullable(this.connectTimeout);
    }

    @Override
    public Optional<Duration> getReadTimeout() {
        return Optional.ofNullable(this.readTimeout);
    }

    @Override
    public Optional<Duration> getWriteTimeout() {
        return Optional.ofNullable(this.writeTimeout);
    }

    @Override
    public Optional<Duration> getCallTimeout() {
        return Optional.ofNullable(this.callTimeout);
    }

    @Override
    public Optional<Duration> getPingInterval() {
        return Optional.ofNullable(this.pingInterval);
    }

    @Override
    public Optional<Boolean> getRetryOnConnectionFailure() {
        return Optional.ofNullable(this.retryOnConnectionFailure);
    }
}
