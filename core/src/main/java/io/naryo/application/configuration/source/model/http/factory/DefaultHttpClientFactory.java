package io.naryo.application.configuration.source.model.http.factory;

import java.time.Duration;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import io.naryo.domain.common.http.HttpClient;

import static io.naryo.application.common.util.OptionalUtil.valueOrDefault;

public class DefaultHttpClientFactory implements HttpClientFactory {

    public static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    public static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration DEFAULT_WRITE_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration DEFAULT_CALL_TIMEOUT = Duration.ofSeconds(60);
    public static final Duration DEFAULT_PING_INTERVAL = Duration.ofSeconds(15);
    public static final boolean DEFAULT_RETRY_ON_CONNECTION_FAILURE = true;

    @Override
    public HttpClient create(HttpClientDescriptor descriptor) {
        return new HttpClient(
                valueOrDefault(descriptor.getMaxIdleConnections(), DEFAULT_MAX_IDLE_CONNECTIONS),
                valueOrDefault(descriptor.getKeepAliveDuration(), DEFAULT_KEEP_ALIVE_DURATION),
                valueOrDefault(descriptor.getConnectTimeout(), DEFAULT_CONNECT_TIMEOUT),
                valueOrDefault(descriptor.getReadTimeout(), DEFAULT_READ_TIMEOUT),
                valueOrDefault(descriptor.getWriteTimeout(), DEFAULT_WRITE_TIMEOUT),
                valueOrDefault(descriptor.getCallTimeout(), DEFAULT_CALL_TIMEOUT),
                valueOrDefault(descriptor.getPingInterval(), DEFAULT_PING_INTERVAL),
                valueOrDefault(
                        descriptor.getRetryOnConnectionFailure(),
                        DEFAULT_RETRY_ON_CONNECTION_FAILURE));
    }
}
