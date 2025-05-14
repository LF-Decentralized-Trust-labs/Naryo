package io.librevents.infrastructure.configuration.source.env.serialization;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.source.env.model.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeProperties;
import org.springframework.stereotype.Component;

@Component
public final class MainPropertiesSerializer extends EnvironmentSerializer<EnvironmentProperties> {

    @Override
    public EnvironmentProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        HttpClientProperties httpClient =
                codec.treeToValue(root.get("httpClient"), HttpClientProperties.class);
        BroadcastingProperties broadcasting =
                codec.treeToValue(root.get("broadcasting"), BroadcastingProperties.class);

        JsonNode nodesNode = root.get("nodes");
        List<NodeProperties> nodes = new java.util.ArrayList<>();

        for (JsonNode nodeEntry : nodesNode) {
            nodes.add(codec.treeToValue(nodeEntry, NodeProperties.class));
        }

        return new EnvironmentProperties(httpClient, broadcasting, nodes);
    }
}
