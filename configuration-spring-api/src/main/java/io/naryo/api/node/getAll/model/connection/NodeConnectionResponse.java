package io.naryo.api.node.getAll.model.connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpNodeConnectionResponse.class, name = "HTTP"),
    @JsonSubTypes.Type(value = WsNodeConnectionResponse.class, name = "WS")
})
@Schema(
        description = "Base node connection",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "HTTP", schema = HttpNodeConnectionResponse.class),
            @DiscriminatorMapping(value = "WS", schema = WsNodeConnectionResponse.class)
        })
@Getter
public abstract class NodeConnectionResponse {

    protected final RetryConfigurationResponse retryConfiguration;
    protected final ConnectionEndpointResponse connectionEndpoint;

    protected NodeConnectionResponse(
            ConnectionEndpointResponse connectionEndpoint,
            RetryConfigurationResponse retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
        this.connectionEndpoint = connectionEndpoint;
    }

    public static NodeConnectionResponse fromDomain(NodeConnection nodeConnection) {
        return switch (nodeConnection) {
            case HttpNodeConnection http -> HttpNodeConnectionResponse.fromDomain(http);
            case WsNodeConnection ws -> WsNodeConnectionResponse.fromDomain(ws);
            default -> throw new IllegalStateException("Unexpected value: " + nodeConnection);
        };
    }
}
