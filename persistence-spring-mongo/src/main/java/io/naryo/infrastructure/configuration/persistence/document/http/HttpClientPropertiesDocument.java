package io.naryo.infrastructure.configuration.persistence.document.http;

import java.time.Duration;

import jakarta.annotation.Nullable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "http_clients")
public class HttpClientPropertiesDocument {

    private final @MongoId String id;
    private @Nullable Integer maxIdleConnections;
    private @Nullable Duration keepAliveDuration;
    private @Nullable Duration connectTimeout;
    private @Nullable Duration readTimeout;
    private @Nullable Duration writeTimeout;
    private @Nullable Duration callTimeout;
    private @Nullable Duration pingInterval;
    private @Nullable Boolean retryOnConnectionFailure;

    public HttpClientPropertiesDocument(
            String id,
            Integer maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectTimeout,
            Duration readTimeout,
            Duration writeTimeout,
            Duration callTimeout,
            Duration pingInterval,
            Boolean retryOnConnectionFailure) {
        this.id = id;
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.callTimeout = callTimeout;
        this.pingInterval = pingInterval;
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }
}
