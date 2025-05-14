package io.librevents.application.broadcaster.configuration;

import io.librevents.application.configuration.ConfigurationProvider;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;

public abstract class BroadcasterConfigurationProvider<T extends BroadcasterConfiguration>
        implements ConfigurationProvider<T> {
    protected final BroadcasterType type;

    protected BroadcasterConfigurationProvider(BroadcasterType type) {
        this.type = type;
    }
}
