package io.naryo.api.node.common.request.connection;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "WebSocket node connection request")
@Getter
public final class WsNodeConnectionRequest extends NodeConnectionRequest {

    public WsNodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration) {
        super(connectionEndpoint, retryConfiguration);
    }

    @Override
    public NodeConnection toDomain() {
        RetryConfigurationRequest retry =
                this.retryConfiguration != null
                        ? this.retryConfiguration
                        : new RetryConfigurationRequest(null, null);
        return new WsNodeConnection(this.connectionEndpoint.toDomain(), retry.toDomain());
    }
}
