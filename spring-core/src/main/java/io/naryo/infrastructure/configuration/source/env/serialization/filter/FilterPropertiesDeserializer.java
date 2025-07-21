package io.naryo.infrastructure.configuration.source.env.serialization.filter;

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
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.contract.ContractEventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.global.GlobalEventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.transaction.TransactionFilterProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class FilterPropertiesDeserializer extends EnvironmentDeserializer<FilterProperties> {

    @Override
    public FilterProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
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

        return switch (type) {
            case EVENT -> {
                EventFilterScope scope =
                        safeTreeToValue(root, "scope", codec, EventFilterScope.class);
                EventSpecification specification =
                        safeTreeToValue(root, "specification", codec, EventSpecification.class);
                Set<ContractEventStatus> statuses = getStatuses(root, codec);
                FilterSyncProperties sync =
                        safeTreeToValue(root, "sync", codec, FilterSyncProperties.class);
                EventFilterVisibilityProperties visibility =
                        safeTreeToValue(
                                root, "visibility", codec, EventFilterVisibilityProperties.class);
                yield switch (scope) {
                    case CONTRACT -> {
                        String address = getTextOrNull(root.get("address"));
                        yield new ContractEventFilterProperties(
                                getUuidOrNull(id),
                                name,
                                getUuidOrNull(nodeId),
                                specification,
                                statuses,
                                sync,
                                visibility,
                                address);
                    }
                    case null, default ->
                            new GlobalEventFilterProperties(
                                    getUuidOrNull(id),
                                    name,
                                    getUuidOrNull(nodeId),
                                    specification,
                                    statuses,
                                    sync,
                                    visibility);
                };
            }
            case TRANSACTION -> {
                IdentifierType identifierType =
                        safeTreeToValue(root, "identifierType", codec, IdentifierType.class);
                String value = getTextOrNull(root.get("value"));
                Set<TransactionStatus> statuses = getStatuses(root, codec);
                yield new TransactionFilterProperties(
                        getUuidOrNull(id),
                        name,
                        getUuidOrNull(nodeId),
                        identifierType,
                        value,
                        statuses);
            }
        };
    }

    private <T> Set<T> getStatuses(JsonNode root, ObjectCodec codec) {
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
