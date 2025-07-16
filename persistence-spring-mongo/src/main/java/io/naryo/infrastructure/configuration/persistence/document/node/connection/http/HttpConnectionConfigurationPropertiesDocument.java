package io.naryo.infrastructure.configuration.persistence.document.node.connection.http;

import java.time.Duration;

import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("http_connection")
public class HttpConnectionConfigurationPropertiesDocument extends ConnectionPropertiesDocument {

    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    private int maxIdleConnections;
    private Duration keepAliveDuration;
    private Duration connectionTimeout;
    private Duration readTimeout;

    public int getMaxIdleConnections() {
        return this.maxIdleConnections != 0
                ? this.maxIdleConnections
                : DEFAULT_MAX_IDLE_CONNECTIONS;
    }

    public Duration getKeepAliveDuration() {
        return this.keepAliveDuration != null
                ? this.keepAliveDuration
                : DEFAULT_KEEP_ALIVE_DURATION;
    }

    public Duration getConnectionTimeout() {
        return this.connectionTimeout != null ? this.connectionTimeout : DEFAULT_CONNECTION_TIMEOUT;
    }

    public Duration getReadTimeout() {
        return this.readTimeout != null ? this.readTimeout : DEFAULT_READ_TIMEOUT;
    }
}
