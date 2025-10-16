package io.naryo.api.node.common.request.connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpNodeConnectionRequest.class, name = "HTTP"),
    @JsonSubTypes.Type(value = WsNodeConnectionRequest.class, name = "WS")
})
@Getter
public abstract class NodeConnectionRequest {

    protected final @NotNull NodeConnectionType type;
    protected final RetryConfigurationRequest retryConfiguration;
    protected final ConnectionEndpointRequest connectionEndpoint;

    protected NodeConnectionRequest(
            NodeConnectionType type,
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration) {
        this.type = type;
        this.retryConfiguration = retryConfiguration;
        this.connectionEndpoint = connectionEndpoint;
    }

    public abstract NodeConnection toDomain();
}
