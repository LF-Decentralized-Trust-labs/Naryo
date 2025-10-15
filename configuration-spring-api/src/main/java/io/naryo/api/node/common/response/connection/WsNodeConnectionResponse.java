package io.naryo.api.node.common.response.connection;

import lombok.Builder;

@Builder
public record WsNodeConnectionResponse(
        String type,
        RetryConfigurationResponse retryConfiguration,
        ConnectionEndpointResponse connectionEndpoint)
        implements NodeConnectionResponse {}
