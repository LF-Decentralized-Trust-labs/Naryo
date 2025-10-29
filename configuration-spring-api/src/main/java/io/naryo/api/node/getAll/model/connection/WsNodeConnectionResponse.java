package io.naryo.api.node.getAll.model.connection;

import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "WebSocket node connection")
@Getter
public final class WsNodeConnectionResponse extends NodeConnectionResponse {

    public WsNodeConnectionResponse(
            ConnectionEndpointResponse connectionEndpoint,
            RetryConfigurationResponse retryConfiguration) {
        super(connectionEndpoint, retryConfiguration);
    }

    public static WsNodeConnectionResponse fromDomain(WsNodeConnection wsNodeConnection) {
        return new WsNodeConnectionResponse(
                ConnectionEndpointResponse.fromDomain(wsNodeConnection.getEndpoint()),
                RetryConfigurationResponse.fromDomain(wsNodeConnection.getRetryConfiguration()));
    }
}
