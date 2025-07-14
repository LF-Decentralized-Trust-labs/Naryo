package io.naryo.application.broadcaster.configuration.mapper;

import java.util.*;

import io.naryo.application.configuration.mapper.ConfigurationMapper;
import io.naryo.application.configuration.mapper.ConfigurationMapperRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;

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
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        ConfigurationMapper<BroadcasterConfiguration, ?> mapper =
                findCompatibleMapper(typeRegistry, sourceClass);

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No mapper registered for type " + type + " and source " + sourceClass);
        }

        return ((ConfigurationMapper<BroadcasterConfiguration, S>) mapper).map(source);
    }

    private ConfigurationMapper<BroadcasterConfiguration, ?> findCompatibleMapper(
            Map<Class<?>, ConfigurationMapper<BroadcasterConfiguration, ?>> typeRegistry,
            Class<?> sourceClass) {

        ConfigurationMapper<BroadcasterConfiguration, ?> mapper = typeRegistry.get(sourceClass);
        if (mapper != null) return mapper;

        for (Class<?> clazz : getAllTypes(sourceClass)) {
            mapper = typeRegistry.get(clazz);
            if (mapper != null) return mapper;
        }

        return null;
    }

    private Set<Class<?>> getAllTypes(Class<?> clazz) {
        Set<Class<?>> result = new LinkedHashSet<>();
        Queue<Class<?>> queue = new LinkedList<>();
        queue.add(clazz);

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            Class<?> superclass = current.getSuperclass();
            if (superclass != null && result.add(superclass)) {
                queue.add(superclass);
            }
            for (Class<?> iface : current.getInterfaces()) {
                if (result.add(iface)) {
                    queue.add(iface);
                }
            }
        }

        return result;
    }
}
