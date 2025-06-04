package io.librevents.infrastructure.configuration.source.env.serialization.filter;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.filter.FilterType;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class FilterPropertiesDeserializer extends EnvironmentDeserializer<FilterProperties> {

    @Override
    public FilterProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = root.get("id").asText();
        String name = root.get("name").asText();
        FilterType type = FilterType.valueOf(root.get("type").asText());
        String nodeId = root.get("nodeId").asText();
        FilterConfigurationProperties configuration =
                codec.treeToValue(
                        root.get("configuration"),
                        switch (type) {
                            case EVENT -> EventFilterConfigurationProperties.class;
                            case TRANSACTION -> null;
                        });

        return new FilterProperties(
                UUID.fromString(id), name, type, UUID.fromString(nodeId), configuration);
    }
}
