package io.naryo.infrastructure.configuration.source.env.serialization.filter;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.filter.FilterType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.transaction.TransactionFilterConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class FilterPropertiesDeserializer extends EnvironmentDeserializer<FilterProperties> {

    @Override
    public FilterProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String name = getTextOrNull(root.get("name"));
        String typeStr = getTextOrNull(root.get("type"));
        FilterType type =
                typeStr != null && !typeStr.isBlank()
                        ? FilterType.valueOf(typeStr.toUpperCase())
                        : FilterType.EVENT;
        String nodeId = getTextOrNull(root.get("nodeId"));
        FilterConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (type) {
                            case EVENT -> EventFilterConfigurationProperties.class;
                            case TRANSACTION -> TransactionFilterConfigurationProperties.class;
                        });

        return new FilterProperties(
                getUuidOrNull(id), name, type, getUuidOrNull(nodeId), configuration);
    }
}
