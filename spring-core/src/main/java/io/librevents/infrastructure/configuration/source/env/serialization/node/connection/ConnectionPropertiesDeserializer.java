package io.librevents.infrastructure.configuration.source.env.serialization.node.connection;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.ConnectionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.RetryConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.http.HttpConnectionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.ws.WsConnectionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class ConnectionPropertiesDeserializer
        extends EnvironmentSerializer<ConnectionProperties> {

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
