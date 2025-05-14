package io.librevents.infrastructure.configuration;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.node.NodeProperties;

public final class MainPropertiesSerializer extends JsonDeserializer<MainProperties> {

    @Override
    public MainProperties deserialize(JsonParser p, DeserializationContext context)
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

        return new MainProperties(httpClient, broadcasting, nodes);
    }
}
