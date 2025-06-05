package io.librevents.infrastructure.configuration.source.env.serialization;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.librevents.infrastructure.configuration.source.env.model.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvironmentPropertiesDeserializer
        extends EnvironmentDeserializer<EnvironmentProperties> {

    @Override
    public EnvironmentProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        HttpClientProperties httpClient =
                safeTreeToValue(root, "httpClient", codec, HttpClientProperties.class);
        BroadcastingProperties broadcasting =
                safeTreeToValue(root, "broadcasting", codec, BroadcastingProperties.class);
        List<NodeProperties> nodes = safeTreeToList(root, "nodes", codec, NodeProperties.class);
        List<FilterProperties> filters =
                safeTreeToList(root, "filters", codec, FilterProperties.class);

        return new EnvironmentProperties(httpClient, broadcasting, nodes, filters);
    }
}
