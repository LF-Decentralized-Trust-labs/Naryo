package io.naryo.domain.common.http;

import java.time.Duration;
import java.util.Objects;

public record HttpClient(
        int maxIdleConnections,
        Duration keepAliveDuration,
        Duration connectTimeout,
        Duration readTimeout,
        Duration writeTimeout,
        Duration callTimeout,
        Duration pingInterval,
        boolean retryOnConnectionFailure) {

    public HttpClient {
        Objects.requireNonNull(keepAliveDuration, "keepAliveDuration");
        Objects.requireNonNull(connectTimeout, "connectTimeout");
        Objects.requireNonNull(readTimeout, "readTimeout");
        Objects.requireNonNull(writeTimeout, "writeTimeout");
        Objects.requireNonNull(callTimeout, "callTimeout");
        Objects.requireNonNull(pingInterval, "pingInterval");
        if (maxIdleConnections < 0)
            throw new IllegalArgumentException("maxIdleConnections must be >= 0");
        if (keepAliveDuration.isNegative())
            throw new IllegalArgumentException("keepAliveDuration must be positive");
        if (connectTimeout.isNegative() || connectTimeout.isZero())
            throw new IllegalArgumentException("connectTimeout must be > 0");
        if (readTimeout.isNegative() || readTimeout.isZero())
            throw new IllegalArgumentException("readTimeout must be > 0");
        if (writeTimeout.isNegative() || writeTimeout.isZero())
            throw new IllegalArgumentException("writeTimeout must be > 0");
        if (callTimeout.isNegative() || callTimeout.isZero())
            throw new IllegalArgumentException("callTimeout must be > 0");
        if (pingInterval.isNegative() || pingInterval.isZero())
            throw new IllegalArgumentException("pingInterval must be > 0");
    }
}
