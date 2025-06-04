package io.librevents.infrastructure.configuration.source.env.model.filter.event.sync;

public record SyncConfigurationProperties(
        SyncType type, SyncConfigurationAdditionalProperties configuration) {}
