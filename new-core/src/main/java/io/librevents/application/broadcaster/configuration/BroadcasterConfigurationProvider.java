package io.librevents.application.broadcaster.configuration;

import io.librevents.application.configuration.ConfigurationProvider;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;

public abstract class BroadcasterConfigurationProvider
        implements ConfigurationProvider<BroadcasterConfiguration> {
    protected final BroadcasterType type;

    protected BroadcasterConfigurationProvider(BroadcasterType type) {
        this.type = type;
    }
}
