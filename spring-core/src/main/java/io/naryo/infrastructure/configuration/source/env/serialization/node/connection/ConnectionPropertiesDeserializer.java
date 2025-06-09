package io.naryo.infrastructure.configuration.source.env.serialization.node.connection;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.RetryConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.http.HttpConnectionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ws.WsConnectionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class ConnectionPropertiesDeserializer
        extends EnvironmentDeserializer<ConnectionProperties> {

    @Override
    public ConnectionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String typeStr = getTextOrNull(root.get("type"));
        NodeConnectionType type =
                typeStr != null && !typeStr.isBlank()
                        ? NodeConnectionType.valueOf(typeStr.toUpperCase())
                        : NodeConnectionType.HTTP;
        RetryConfigurationProperties retry =
                safeTreeToValue(root, "retry", codec, RetryConfigurationProperties.class);
        ConnectionEndpointProperties endpoint =
                safeTreeToValue(root, "endpoint", codec, ConnectionEndpointProperties.class);
        ConnectionConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (type) {
                            case HTTP -> HttpConnectionConfigurationProperties.class;
                            case WS -> WsConnectionConfigurationProperties.class;
                        });

        return new ConnectionProperties(type, retry, endpoint, configuration);
    }
}
