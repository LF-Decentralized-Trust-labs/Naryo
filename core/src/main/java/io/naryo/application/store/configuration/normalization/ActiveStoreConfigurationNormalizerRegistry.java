package io.naryo.application.store.configuration.normalization;

import java.util.*;

import io.naryo.application.configuration.normalization.ConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.normalization.Normalizer;

public final class ActiveStoreConfigurationNormalizerRegistry
        implements ConfigurationNormalizerRegistry<ActiveStoreConfiguration> {

    private final Map<String, Map<Class<?>, Normalizer<ActiveStoreConfiguration>>> registry =
            new HashMap<>();

    @Override
    public <S> void register(
            String type, Class<S> sourceClass, Normalizer<ActiveStoreConfiguration> normalizer) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, normalizer);
    }

    @Override
    public <S> ActiveStoreConfiguration normalize(String type, S source) {
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        Normalizer<ActiveStoreConfiguration> normalizer =
                findCompatibleNormalizer(typeRegistry, sourceClass);

        if (normalizer == null) {
            return (ActiveStoreConfiguration) source;
        }

        return normalizer.normalize((ActiveStoreConfiguration) source);
    }

    private Normalizer<ActiveStoreConfiguration> findCompatibleNormalizer(
            Map<Class<?>, Normalizer<ActiveStoreConfiguration>> typeRegistry,
            Class<?> sourceClass) {

        Normalizer<ActiveStoreConfiguration> normalizer = typeRegistry.get(sourceClass);
        if (normalizer != null) return normalizer;

        for (Class<?> clazz : getAllTypes(sourceClass)) {
            normalizer = typeRegistry.get(clazz);
            if (normalizer != null) return normalizer;
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
