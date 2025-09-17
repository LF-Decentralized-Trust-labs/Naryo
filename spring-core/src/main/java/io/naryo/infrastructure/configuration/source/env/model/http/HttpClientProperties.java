package io.naryo.infrastructure.configuration.source.env.model.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class HttpClientProperties implements HttpClientDescriptor {

    private @Setter @Nullable Integer maxIdleConnections;

    private @Setter @Nullable Duration keepAliveDuration;

    private @Setter @Nullable Duration connectTimeout;

    private @Setter @Nullable Duration readTimeout;

    private @Setter @Nullable Duration writeTimeout;

    private @Setter @Nullable Duration callTimeout;

    private @Setter @Nullable Duration pingInterval;

    private @Setter @Nullable Boolean retryOnConnectionFailure;

    public HttpClientProperties(
            int maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectTimeout,
            Duration readTimeout,
            Duration writeTimeout,
            Duration callTimeout,
            Duration pingInterval,
            boolean retryOnConnectionFailure) {
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.callTimeout = callTimeout;
        this.pingInterval = pingInterval;
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    @Override
    public Optional<Integer> getMaxIdleConnections() {
        return Optional.ofNullable(maxIdleConnections);
    }

    @Override
    public Optional<Duration> getKeepAliveDuration() {
        return Optional.ofNullable(keepAliveDuration);
    }

    @Override
    public Optional<Duration> getConnectTimeout() {
        return Optional.ofNullable(connectTimeout);
    }

    @Override
    public Optional<Duration> getReadTimeout() {
        return Optional.ofNullable(readTimeout);
    }

    @Override
    public Optional<Duration> getWriteTimeout() {
        return Optional.ofNullable(writeTimeout);
    }

    @Override
    public Optional<Duration> getCallTimeout() {
        return Optional.ofNullable(callTimeout);
    }

    @Override
    public Optional<Duration> getPingInterval() {
        return Optional.ofNullable(pingInterval);
    }

    @Override
    public Optional<Boolean> getRetryOnConnectionFailure() {
        return Optional.ofNullable(retryOnConnectionFailure);
    }
}
