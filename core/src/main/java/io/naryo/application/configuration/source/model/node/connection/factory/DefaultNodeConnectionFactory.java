package io.naryo.application.configuration.source.model.node.connection.factory;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public class DefaultNodeConnectionFactory implements NodeConnectionFactory {

    public static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    public static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    public static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    public static final int DEFAULT_RETRY_TIMES = 3;
    public static final Duration DEFAULT_RETRY_BACKOFF = Duration.ofSeconds(30);

    @Override
    public NodeConnection create(NodeConnectionDescriptor descriptor) {

        var endpoint =
                buildConnectionEndpoint(
                        valueOrNull(NodeConnectionDescriptor::getEndpoint, descriptor));
        var retryDescriptor = valueOrNull(NodeConnectionDescriptor::getRetry, descriptor);

        var retry =
                (retryDescriptor == null)
                        ? new RetryConfiguration(DEFAULT_RETRY_TIMES, DEFAULT_RETRY_BACKOFF)
                        : buildRetry(applyRetryDefaults(retryDescriptor));

        return switch (descriptor.getType()) {
            case HTTP -> {
                var httpConnection = (HttpNodeConnectionDescriptor) descriptor;
                applyHttpDefaults(httpConnection);
                yield new HttpNodeConnection(
                        endpoint,
                        retry,
                        new MaxIdleConnections(
                                httpConnection.getMaxIdleConnections().orElseThrow()),
                        new KeepAliveDuration(httpConnection.getKeepAliveDuration().orElseThrow()),
                        new ConnectionTimeout(httpConnection.getConnectionTimeout().orElseThrow()),
                        new ReadTimeout(httpConnection.getReadTimeout().orElseThrow()));
            }
            case WS -> new WsNodeConnection(endpoint, retry);
        };
    }

    private static void applyHttpDefaults(HttpNodeConnectionDescriptor descriptor) {
        setDefault(
                descriptor::getMaxIdleConnections,
                descriptor::setMaxIdleConnections,
                DEFAULT_MAX_IDLE_CONNECTIONS);
        setDefault(
                descriptor::getKeepAliveDuration,
                descriptor::setKeepAliveDuration,
                DEFAULT_KEEP_ALIVE_DURATION);
        setDefault(
                descriptor::getConnectionTimeout,
                descriptor::setConnectionTimeout,
                DEFAULT_CONNECTION_TIMEOUT);
        setDefault(descriptor::getReadTimeout, descriptor::setReadTimeout, DEFAULT_READ_TIMEOUT);
    }

    private static NodeConnectionRetryDescriptor applyRetryDefaults(
            NodeConnectionRetryDescriptor descriptor) {
        setDefault(descriptor::getTimes, descriptor::setTimes, DEFAULT_RETRY_TIMES);
        setDefault(descriptor::getBackoff, descriptor::setBackoff, DEFAULT_RETRY_BACKOFF);
        return descriptor;
    }

    private static ConnectionEndpoint buildConnectionEndpoint(
            ConnectionEndpointDescriptor descriptor) {
        return descriptor != null ? new ConnectionEndpoint(valueOrNull(descriptor.getUrl())) : null;
    }

    private static RetryConfiguration buildRetry(NodeConnectionRetryDescriptor descriptor) {
        return descriptor != null
                ? new RetryConfiguration(
                        valueOrNull(descriptor.getTimes()), valueOrNull(descriptor.getBackoff()))
                : null;
    }

    private static <T> void setDefault(
            Supplier<Optional<T>> getter, Consumer<T> setter, T defaultValue) {
        setter.accept(getter.get().orElse(defaultValue));
    }
}
