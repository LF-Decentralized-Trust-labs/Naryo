package io.librevents.infrastructure.configuration.source.env.registry.broadcaster;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class BroadcasterConfigurationRegistry {

    private final Map<String, Class<? extends BroadcasterConfigurationAdditionalProperties>>
            typeMappings = new HashMap<>();

    public void register(
            String type, Class<? extends BroadcasterConfigurationAdditionalProperties> clazz) {
        typeMappings.put(type.toUpperCase(), clazz);
    }

    public Class<? extends BroadcasterConfigurationAdditionalProperties> get(String type) {
        return typeMappings.get(type.toUpperCase());
    }
}
