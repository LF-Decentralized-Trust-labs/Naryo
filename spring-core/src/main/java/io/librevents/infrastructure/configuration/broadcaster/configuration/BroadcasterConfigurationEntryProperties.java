package io.librevents.infrastructure.configuration.broadcaster.configuration;

public record BroadcasterConfigurationEntryProperties(
        String name, String type, BroadcasterConfigurationAdditionalProperties configuration) {}
