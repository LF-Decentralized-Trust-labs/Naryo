package io.naryo.application.broadcaster.configuration.mapper;

import java.util.*;

import io.naryo.application.configuration.mapper.AdditionalPropertiesMapper;
import io.naryo.application.configuration.mapper.AdditionalPropertiesMapperRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;

public final class BroadcasterConfigurationAdditionalPropertiesMapperRegistry
        implements AdditionalPropertiesMapperRegistry<BroadcasterConfiguration> {

    private final Map<String, Map<Class<?>, AdditionalPropertiesMapper<?>>> registry =
            new HashMap<>();

    @Override
    public <S extends BroadcasterConfiguration> void register(
            String type, Class<S> sourceClass, AdditionalPropertiesMapper<S> mapper) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, mapper);
    }

    @Override
    public <S extends BroadcasterConfiguration> Map<String, Object> map(String type, S source) {
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        AdditionalPropertiesMapper<S> mapper = findCompatibleMapper(typeRegistry, sourceClass);

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No mapper registered for type " + type + " and source " + sourceClass);
        }

        return mapper.map(source);
    }

    private <S extends BroadcasterConfiguration> AdditionalPropertiesMapper<S> findCompatibleMapper(
            Map<Class<?>, AdditionalPropertiesMapper<?>> typeRegistry, Class<?> sourceClass) {

        AdditionalPropertiesMapper<?> mapper = typeRegistry.get(sourceClass);

        return mapper == null ? null : (AdditionalPropertiesMapper<S>) mapper;
    }
}
