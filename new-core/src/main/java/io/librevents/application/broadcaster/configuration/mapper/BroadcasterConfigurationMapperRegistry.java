package io.librevents.application.broadcaster.configuration.mapper;

import java.util.HashMap;
import java.util.Map;

import io.librevents.application.configuration.mapper.ConfigurationMapper;
import io.librevents.application.configuration.mapper.ConfigurationMapperRegistry;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;

public final class BroadcasterConfigurationMapperRegistry
        implements ConfigurationMapperRegistry<BroadcasterConfiguration> {

    private final Map<String, Map<Class<?>, ConfigurationMapper<BroadcasterConfiguration, ?>>>
            registry = new HashMap<>();

    @Override
    public <S> void register(
            String type,
            Class<S> sourceClass,
            ConfigurationMapper<BroadcasterConfiguration, S> mapper) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, mapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> BroadcasterConfiguration map(String type, S source) {
        var mapper = registry.getOrDefault(type.toLowerCase(), Map.of()).get(source.getClass());

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No mapper registered for type " + type + " and source " + source.getClass());
        }

        return ((ConfigurationMapper<BroadcasterConfiguration, S>) mapper).map(source);
    }
}
