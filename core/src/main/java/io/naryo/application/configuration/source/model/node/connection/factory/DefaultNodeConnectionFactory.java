package io.naryo.application.configuration.source.model.node.connection.factory;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.connection.ws.WsNodeConnection;

import static io.naryo.application.common.util.OptionalUtil.valueOrDefault;
import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public class DefaultNodeConnectionFactory implements NodeConnectionFactory {

    public static final int DEFAULT_HTTP_MAX_IDLE_CONNECTIONS = 5;
    public static final Duration DEFAULT_HTTP_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    public static final Duration DEFAULT_HTTP_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_HTTP_READ_TIMEOUT = Duration.ofSeconds(30);

    public static final int DEFAULT_RETRY_TIMES = 3;
    public static final Duration DEFAULT_RETRY_BACKOFF = Duration.ofSeconds(30);

    @Override
    public NodeConnection create(NodeConnectionDescriptor descriptor) {
        return switch (descriptor) {
            case HttpNodeConnectionDescriptor httpNodeConnectionDescriptor ->
                    buildHttpNodeConnection(httpNodeConnectionDescriptor);
            case WsNodeConnectionDescriptor wsNodeConnectionDescriptor ->
                    buildWsNodeConnection(wsNodeConnectionDescriptor);
            default -> throw new IllegalStateException("Unexpected value: " + descriptor);
        };
    }

    private static HttpNodeConnection buildHttpNodeConnection(
            HttpNodeConnectionDescriptor descriptor) {
        ConnectionEndpoint endpoint = buildConnectionEndpoint(descriptor.getEndpoint());
        RetryConfiguration retry = buildRetry(descriptor.getRetry());
        MaxIdleConnections maxIdleConnections =
                new MaxIdleConnections(
                        valueOrDefault(
                                descriptor.getMaxIdleConnections(),
                                DEFAULT_HTTP_MAX_IDLE_CONNECTIONS));
        KeepAliveDuration keepAliveDuration =
                new KeepAliveDuration(
                        valueOrDefault(
                                descriptor.getKeepAliveDuration(),
                                DEFAULT_HTTP_KEEP_ALIVE_DURATION));
        ConnectionTimeout connectionTimeout =
                new ConnectionTimeout(
                        valueOrDefault(
                                descriptor.getConnectionTimeout(),
                                DEFAULT_HTTP_CONNECTION_TIMEOUT));
        ReadTimeout readTimeout =
                new ReadTimeout(
                        valueOrDefault(descriptor.getReadTimeout(), DEFAULT_HTTP_READ_TIMEOUT));

        return new HttpNodeConnection(
                endpoint,
                retry,
                maxIdleConnections,
                keepAliveDuration,
                connectionTimeout,
                readTimeout);
    }

    private static WsNodeConnection buildWsNodeConnection(WsNodeConnectionDescriptor descriptor) {
        ConnectionEndpoint endpoint = buildConnectionEndpoint(descriptor.getEndpoint());
        RetryConfiguration retry = buildRetry(descriptor.getRetry());

        return new WsNodeConnection(endpoint, retry);
    }

    private static ConnectionEndpoint buildConnectionEndpoint(
            Optional<ConnectionEndpointDescriptor> descriptor) {
        return descriptor
                .map(val -> new ConnectionEndpoint(valueOrNull(val.getUrl())))
                .orElse(null);
    }

    private static RetryConfiguration buildRetry(
            Optional<NodeConnectionRetryDescriptor> descriptor) {
        return descriptor
                .map(
                        val ->
                                new RetryConfiguration(
                                        valueOrDefault(val.getTimes(), DEFAULT_RETRY_TIMES),
                                        valueOrDefault(val.getBackoff(), DEFAULT_RETRY_BACKOFF)))
                .orElseGet(
                        () -> new RetryConfiguration(DEFAULT_RETRY_TIMES, DEFAULT_RETRY_BACKOFF));
    }
}
