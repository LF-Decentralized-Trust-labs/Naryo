package io.librevents.application.broadcaster.configuration;

import java.util.*;

import io.librevents.application.broadcaster.BroadcasterProducer;
import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;

public final class DefaultBroadcasterConfigurationManager
        implements BroadcasterConfigurationManager {

    private final Set<BroadcasterConfigurationProvider<?>> configurationProviders;
    private final Set<BroadcasterProducer> producers;
    private final Map<String, BroadcasterConfiguration> configurations = new HashMap<>();
    private final Set<BroadcasterWrapper> wrappers = new HashSet<>();

    public DefaultBroadcasterConfigurationManager(
            Set<BroadcasterConfigurationProvider<?>> configurationProviders,
            Set<BroadcasterProducer> producers) {
        Objects.requireNonNull(configurationProviders, "configurationProviders must not be null");
        Objects.requireNonNull(producers, "producers must not be null");

        this.configurationProviders = configurationProviders;
        this.producers = producers;
    }

    @Override
    public void registerConfiguration(BroadcasterType type, String name, Map<String, Object> data) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(data, "data must not be null");

        var provider =
                configurationProviders.stream()
                        .filter(p -> p.supports(type))
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No provider found for type: " + type));

        configurations.put(name, provider.create(data));
    }

    @Override
    public BroadcasterConfiguration getConfiguration(String name) {
        return configurations.get(name);
    }

    @Override
    public void registerBroadcaster(String configurationName, BroadcasterTarget target) {
        Objects.requireNonNull(configurationName, "configurationName must not be null");
        Objects.requireNonNull(target, "target must not be null");

        var configuration = getConfiguration(configurationName);
        if (configuration == null) {
            throw new IllegalArgumentException(
                    "No configuration found for name: " + configurationName);
        }
        var producer =
                producers.stream()
                        .filter(p -> p.supports(configuration.getType()))
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No producer found for type: "
                                                        + configuration.getType()));

        var wrapper = new BroadcasterWrapper(new Broadcaster(target, configuration), producer);
        wrappers.add(wrapper);
    }

    @Override
    public Set<BroadcasterWrapper> wrappers() {
        return wrappers;
    }
}
