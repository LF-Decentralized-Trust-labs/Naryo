package io.naryo.domain.common.http;

import java.time.Duration;
import java.util.Objects;

import io.naryo.domain.normalization.Normalizer;

public final class HttpClientNormalizer implements Normalizer<HttpClient> {

    public static final HttpClientNormalizer INSTANCE = new HttpClientNormalizer();

    public HttpClientNormalizer() {}

    @Override
    public HttpClient normalize(HttpClient in) {
        Objects.requireNonNull(in, "in");

        int maxIdleConnections = Math.max(0, in.maxIdleConnections());

        Duration keepAliveDuration = positive(in.keepAliveDuration());
        Duration connectTimeout = positive(in.connectTimeout());
        Duration readTimeout = positive(in.readTimeout());
        Duration writeTimeout = positive(in.writeTimeout());
        Duration callTimeout = positive(in.callTimeout());
        Duration pingInterval = positive(in.pingInterval());

        boolean retryOnConnectionFailure = in.retryOnConnectionFailure();

        if (maxIdleConnections == in.maxIdleConnections()
                && keepAliveDuration.equals(in.keepAliveDuration())
                && connectTimeout.equals(in.connectTimeout())
                && readTimeout.equals(in.readTimeout())
                && writeTimeout.equals(in.writeTimeout())
                && callTimeout.equals(in.callTimeout())
                && pingInterval.equals(in.pingInterval())
                && retryOnConnectionFailure == in.retryOnConnectionFailure()) {
            return in;
        }

        return new HttpClient(
                maxIdleConnections,
                keepAliveDuration,
                connectTimeout,
                readTimeout,
                writeTimeout,
                callTimeout,
                pingInterval,
                retryOnConnectionFailure);
    }

    private static Duration positive(Duration d) {
        if (d == null) return Duration.ofMillis(1);
        if (d.isNegative() || d.isZero()) return Duration.ofMillis(1);
        return d;
    }
}
