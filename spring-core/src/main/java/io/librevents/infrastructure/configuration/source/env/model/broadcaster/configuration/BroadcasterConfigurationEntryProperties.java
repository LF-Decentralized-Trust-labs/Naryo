package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration;

public record BroadcasterConfigurationEntryProperties(
        String name, String type, BroadcasterConfigurationAdditionalProperties configuration) {}
