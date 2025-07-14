package io.naryo.infrastructure.configuration.source.env.serialization.node.connection;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.NodeConnectionRetryProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.http.HttpConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ws.WsConnectionProperties;
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
        NodeConnectionRetryProperties retry =
                safeTreeToValue(root, "retry", codec, NodeConnectionRetryProperties.class);
        ConnectionEndpointProperties endpoint =
                safeTreeToValue(root, "endpoint", codec, ConnectionEndpointProperties.class);

        return switch (type) {
            case HTTP -> {
                Integer maxIdleConnections =
                        safeTreeToValue(root, "maxIdleConnections", codec, Integer.class);
                Duration keepAliveDuration =
                        safeTreeToValue(root, "keepAliveDuration", codec, Duration.class);
                Duration connectTimeout =
                        safeTreeToValue(root, "connectTimeout", codec, Duration.class);
                Duration readTimeout = safeTreeToValue(root, "readTimeout", codec, Duration.class);
                yield new HttpConnectionProperties(
                        retry,
                        endpoint,
                        maxIdleConnections,
                        keepAliveDuration,
                        connectTimeout,
                        readTimeout);
            }
            case WS -> new WsConnectionProperties(retry, endpoint);
        };
    }
}
