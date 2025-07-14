package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvBroadcasterConfigurationSourceProvider
        implements BroadcasterConfigurationSourceProvider {

    private final EnvironmentProperties properties;

    public EnvBroadcasterConfigurationSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<BroadcasterConfigurationDescriptor> load() {
        return new HashSet<>(properties.broadcasting().configuration());
    }

    @Override
    public int priority() {
        return 0;
    }
}
