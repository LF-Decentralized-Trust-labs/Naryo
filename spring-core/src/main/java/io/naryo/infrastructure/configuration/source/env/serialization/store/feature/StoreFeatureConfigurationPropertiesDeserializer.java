package io.naryo.infrastructure.configuration.source.env.serialization.store.feature;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.source.env.model.store.StoreFeatureConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.event.EventStoreTargetProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.event.block.BlockEventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.filter.FilterStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class StoreFeatureConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<StoreFeatureConfigurationProperties> {

    @Override
    public StoreFeatureConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        StoreFeatureType type = safeTreeToValue(root, "type", codec, StoreFeatureType.class);
        return switch (type) {
            case EVENT -> {
                EventStoreStrategy strategy =
                        safeTreeToValue(root, "strategy", codec, EventStoreStrategy.class);

                yield switch (strategy) {
                    case BLOCK_BASED -> {
                        List<EventStoreTargetProperties> targets =
                                safeTreeToList(
                                        root, "targets", codec, EventStoreTargetProperties.class);
                        yield new BlockEventStoreConfigurationProperties(new HashSet<>(targets));
                    }
                };
            }
            case FILTER_SYNC -> {
                String destination = getTextOrNull(root.get("destination"));
                yield new FilterStoreConfigurationProperties(destination);
            }
        };
    }
}
