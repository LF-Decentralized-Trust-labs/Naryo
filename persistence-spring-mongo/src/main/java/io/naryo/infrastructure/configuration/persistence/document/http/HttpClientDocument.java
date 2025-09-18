package io.naryo.infrastructure.configuration.persistence.document.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "http_client")
@AllArgsConstructor
@Setter
public class HttpClientDocument implements HttpClientDescriptor {

    private final @MongoId String id;

    private @Nullable Integer maxIdleConnections;

    private @Nullable Duration keepAliveDuration;

    private @Nullable Duration connectTimeout;

    private @Nullable Duration readTimeout;

    private @Nullable Duration writeTimeout;

    private @Nullable Duration callTimeout;

    private @Nullable Duration pingInterval;

    private @Nullable Boolean retryOnConnectionFailure;

    @Override
    public Optional<Integer> getMaxIdleConnections() {
        return Optional.ofNullable(this.maxIdleConnections);
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
