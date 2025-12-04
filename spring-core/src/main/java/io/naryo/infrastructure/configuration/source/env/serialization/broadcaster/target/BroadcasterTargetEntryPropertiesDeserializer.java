package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster.target;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.*;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterTargetEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterEntryProperties> {

    @Override
    public BroadcasterEntryProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String configurationId = getTextOrNull(root.get("configurationId"));
        JsonNode targetNode = root.get("target");
        String typeString = getTextOrNull(targetNode != null ? targetNode.get("type") : null);
        BroadcasterTargetType type =
                typeString != null && !typeString.isBlank()
                        ? BroadcasterTargetType.valueOf(typeString.toUpperCase())
                        : BroadcasterTargetType.ALL;
        Set<String> destinations = getDestinations(root.get("target"), codec);
        BroadcasterTargetDescriptor target =
                switch (type) {
                    case ALL -> new AllBroadcasterTargetProperties(destinations);
                    case TRANSACTION ->
                            new TransactionBroadcasterTargetConfigurationProperties(destinations);
                    case CONTRACT_EVENT ->
                            new ContractEventBroadcasterTargetProperties(destinations);
                    case FILTER -> {
                        String filterId = getTextOrNull(targetNode.get("filterId"));
                        yield new FilterBroadcasterTargetProperties(
                                destinations, getUuidOrNull(filterId));
                    }
                    case BLOCK -> new BlockBroadcasterTargetProperties(destinations);
                };

        return new BroadcasterEntryProperties(
                getUuidOrNull(id), getUuidOrNull(configurationId), target);
    }

    private Set<String> getDestinations(JsonNode root, ObjectCodec codec) {
        ObjectMapper mapper = (ObjectMapper) codec;
        JsonNode destinationsNode = root.get("destinations");
        ArrayNode arrayNode;

        if (destinationsNode == null || destinationsNode.isNull()) {
            arrayNode = mapper.createArrayNode(); // empty list
        } else if (destinationsNode.isArray()) {
            arrayNode = (ArrayNode) destinationsNode;
        } else {
            ObjectNode obj = (ObjectNode) destinationsNode;
            List<String> idxs =
                    StreamSupport.stream(
                                    Spliterators.spliteratorUnknownSize(
                                            obj.fieldNames(), Spliterator.ORDERED),
                                    false)
                            .sorted(Comparator.comparingInt(Integer::parseInt))
                            .toList();

            arrayNode = mapper.createArrayNode();
            for (String idx : idxs) {
                arrayNode.add(obj.get(idx));
            }
        }

        return mapper.convertValue(arrayNode, new TypeReference<>() {});
    }
}
