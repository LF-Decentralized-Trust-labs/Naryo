package io.naryo.api.node.common.request.connection;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "WebSocket node connection")
@Getter
public final class WsNodeConnectionRequest extends NodeConnectionRequest {

    public WsNodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration) {
        super(connectionEndpoint, retryConfiguration);
    }

    @Override
    public NodeConnection toDomain() {
        return new WsNodeConnection(
                this.connectionEndpoint.toDomain(), this.retryConfiguration.toDomain());
    }
}
