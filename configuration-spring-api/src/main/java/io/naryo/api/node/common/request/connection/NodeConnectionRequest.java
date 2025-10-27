package io.naryo.api.node.common.request.connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.connection.NodeConnection;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpNodeConnectionRequest.class, name = "HTTP"),
    @JsonSubTypes.Type(value = WsNodeConnectionRequest.class, name = "WS")
})
@Schema(
        description = "Base class for node connection request",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(value = "HTTP", schema = HttpNodeConnectionRequest.class),
            @DiscriminatorMapping(value = "WS", schema = WsNodeConnectionRequest.class)
        })
@Getter
public abstract class NodeConnectionRequest {

    protected final RetryConfigurationRequest retryConfiguration;
    protected final ConnectionEndpointRequest connectionEndpoint;

    protected NodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
        this.connectionEndpoint = connectionEndpoint;
    }

    public abstract NodeConnection toDomain();
}
