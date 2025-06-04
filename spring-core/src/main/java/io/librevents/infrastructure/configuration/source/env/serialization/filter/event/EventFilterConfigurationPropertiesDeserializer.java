package io.librevents.infrastructure.configuration.source.env.serialization.filter.event;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.librevents.domain.common.event.ContractEventStatus;
import io.librevents.domain.filter.event.EventFilterScope;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.EventFilterConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.contract.ContractEventFilterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class EventFilterConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<EventFilterConfigurationProperties> {

    @Override
    public EventFilterConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        EventFilterScope scope = EventFilterScope.valueOf(root.get("scope").asText());
        EventSpecification specification =
                codec.treeToValue(root.get("specification"), EventSpecification.class);
        List<ContractEventStatus> statuses = getStatuses(root, codec);
        SyncConfigurationProperties sync =
                codec.treeToValue(root.get("sync"), SyncConfigurationProperties.class);
        EventFilterConfigurationAdditionalProperties configuration = null;
        if (scope.equals(EventFilterScope.CONTRACT)) {
            configuration =
                    codec.treeToValue(
                            root.get("configuration"),
                            ContractEventFilterConfigurationAdditionalProperties.class);
        }

        return new EventFilterConfigurationProperties(
                scope, specification, statuses, sync, configuration);
    }

    private List<ContractEventStatus> getStatuses(JsonNode root, ObjectCodec codec) {
        ObjectMapper mapper = (ObjectMapper) codec;
        JsonNode statusesNode = root.get("statuses");
        ArrayNode arrayNode;

        if (statusesNode == null || statusesNode.isNull()) {
            arrayNode = mapper.createArrayNode(); // empty list
        } else if (statusesNode.isArray()) {
            arrayNode = (ArrayNode) statusesNode;
        } else {
            ObjectNode obj = (ObjectNode) statusesNode;
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
