package io.naryo.api.node.common.request.connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.domain.node.connection.http.*;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpNodeConnectionRequest.class, name = "HTTP"),
    @JsonSubTypes.Type(value = WsNodeConnectionRequest.class, name = "WS")
})
@Getter
public abstract class NodeConnectionRequest {

    private final @NotNull NodeConnectionType type;
    private final RetryConfigurationRequest retryConfiguration;
    private final ConnectionEndpointRequest connectionEndpoint;

    protected NodeConnectionRequest(
            NodeConnectionType type,
            RetryConfigurationRequest retryConfiguration,
            ConnectionEndpointRequest connectionEndpoint) {
        this.type = type;
        this.retryConfiguration = retryConfiguration;
        this.connectionEndpoint = connectionEndpoint;
    }

    public NodeConnection toDomain() {
        return switch (type) {
            case HTTP -> {
                var http = (HttpNodeConnectionRequest) this;
                yield new HttpNodeConnection(
                        this.connectionEndpoint.toDomain(),
                        this.retryConfiguration.toDomain(),
                        new MaxIdleConnections(http.getMaxIdleConnections()),
                        new KeepAliveDuration(http.getKeepAliveDuration()),
                        new ConnectionTimeout(http.getConnectionTimeout()),
                        new ReadTimeout(http.getReadTimeout()));
            }
            case WS ->
                    new WsNodeConnection(
                            this.connectionEndpoint.toDomain(), this.retryConfiguration.toDomain());
        };
    }
}
