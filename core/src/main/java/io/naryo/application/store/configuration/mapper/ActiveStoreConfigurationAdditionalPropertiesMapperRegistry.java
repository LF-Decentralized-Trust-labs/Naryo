package io.naryo.application.store.configuration.mapper;

import java.util.*;

import io.naryo.application.configuration.mapper.AdditionalPropertiesMapper;
import io.naryo.application.configuration.mapper.AdditionalPropertiesMapperRegistry;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;

public final class ActiveStoreConfigurationAdditionalPropertiesMapperRegistry
        implements AdditionalPropertiesMapperRegistry {

    private final Map<String, Map<Class<?>, AdditionalPropertiesMapper<?>>> registry =
            new HashMap<>();

    @Override
    public <S extends ActiveStoreConfiguration> void register(
            String type, Class<S> sourceClass, AdditionalPropertiesMapper<S> mapper) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, mapper);
    }

    @Override
    public <S extends ActiveStoreConfiguration> Map<String, Object> map(String type, S source) {
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        AdditionalPropertiesMapper<S> mapper = findCompatibleMapper(typeRegistry, sourceClass);

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No mapper registered for type " + type + " and source " + sourceClass);
        }

        return mapper.map(source);
    }

    private <S extends ActiveStoreConfiguration> AdditionalPropertiesMapper<S> findCompatibleMapper(
            Map<Class<?>, AdditionalPropertiesMapper<?>> typeRegistry, Class<?> sourceClass) {

        AdditionalPropertiesMapper<?> mapper = typeRegistry.get(sourceClass);

        return mapper == null ? null : (AdditionalPropertiesMapper<S>) mapper;
    }
}
