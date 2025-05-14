package io.librevents.infrastructure.configuration.broadcaster;

import java.util.HashMap;
import java.util.Map;

import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterTypeRegistry {

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
