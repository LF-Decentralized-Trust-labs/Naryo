package io.librevents.infrastructure.configuration.node.connection;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.infrastructure.configuration.common.ConnectionEndpointProperties;
import io.librevents.infrastructure.configuration.node.connection.http.HttpConnectionConfigurationProperties;
import io.librevents.infrastructure.configuration.node.connection.ws.WsConnectionConfigurationProperties;

public final class ConnectionPropertiesDeserializer extends JsonDeserializer<ConnectionProperties> {

    @Override
    public ConnectionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        NodeConnectionType type = NodeConnectionType.valueOf(root.get("type").asText());
        RetryConfigurationProperties retry =
                codec.treeToValue(root.get("retry"), RetryConfigurationProperties.class);
        ConnectionEndpointProperties endpoint =
                codec.treeToValue(root.get("endpoint"), ConnectionEndpointProperties.class);
        JsonNode configurationNode = root.get("configuration");
        ConnectionConfigurationProperties configuration = null;

        switch (type) {
            case HTTP ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode, HttpConnectionConfigurationProperties.class);
            case WS ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode, WsConnectionConfigurationProperties.class);
        }

        return new ConnectionProperties(type, retry, endpoint, configuration);
    }
}
