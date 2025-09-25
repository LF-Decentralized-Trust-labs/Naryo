package io.naryo.application.broadcaster.configuration.normalization;

import java.util.*;

import io.naryo.application.configuration.normalization.ConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.normalization.Normalizer;

public final class BroadcasterConfigurationNormalizerRegistry
        implements ConfigurationNormalizerRegistry<BroadcasterConfiguration> {

    private final Map<String, Map<Class<?>, Normalizer<BroadcasterConfiguration>>> registry =
            new HashMap<>();

    @Override
    public <S> void register(
            String type, Class<S> sourceClass, Normalizer<BroadcasterConfiguration> normalizer) {
        registry.computeIfAbsent(type.toLowerCase(), __ -> new HashMap<>())
                .put(sourceClass, normalizer);
    }

    @Override
    public <S> BroadcasterConfiguration normalize(String type, S source) {
        var typeRegistry = registry.getOrDefault(type.toLowerCase(), Map.of());
        Class<?> sourceClass = source.getClass();

        Normalizer<BroadcasterConfiguration> normalizer =
                findCompatibleNormalizer(typeRegistry, sourceClass);

        if (normalizer == null) {
            return (BroadcasterConfiguration) source;
        }

        return normalizer.normalize((BroadcasterConfiguration) source);
    }

    private Normalizer<BroadcasterConfiguration> findCompatibleNormalizer(
            Map<Class<?>, Normalizer<BroadcasterConfiguration>> typeRegistry,
            Class<?> sourceClass) {

        Normalizer<BroadcasterConfiguration> normalizer = typeRegistry.get(sourceClass);
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
