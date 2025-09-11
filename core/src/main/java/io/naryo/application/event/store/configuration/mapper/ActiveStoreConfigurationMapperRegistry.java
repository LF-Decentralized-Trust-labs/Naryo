package io.naryo.application.event.store.configuration.mapper;

import java.util.*;

import io.naryo.application.configuration.mapper.ConfigurationMapper;
import io.naryo.application.configuration.mapper.ConfigurationMapperRegistry;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;

public final class ActiveStoreConfigurationMapperRegistry
        implements ConfigurationMapperRegistry<ActiveStoreConfiguration> {

    private final Map<String, Map<Class<?>, ConfigurationMapper<ActiveStoreConfiguration, ?>>>
            registry = new HashMap<>();

    @Override
    public <S> void register(
            String type,
            Class<S> sourceClass,
            ConfigurationMapper<ActiveStoreConfiguration, S> mapper) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, mapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> ActiveStoreConfiguration map(String type, S source) {
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        ConfigurationMapper<ActiveStoreConfiguration, ?> mapper =
                findCompatibleMapper(typeRegistry, sourceClass);

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No mapper registered for type " + type + " and source " + sourceClass);
        }

        return ((ConfigurationMapper<ActiveStoreConfiguration, S>) mapper).map(source);
    }

    private ConfigurationMapper<ActiveStoreConfiguration, ?> findCompatibleMapper(
            Map<Class<?>, ConfigurationMapper<ActiveStoreConfiguration, ?>> typeRegistry,
            Class<?> sourceClass) {

        ConfigurationMapper<ActiveStoreConfiguration, ?> mapper = typeRegistry.get(sourceClass);
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
