package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvBroadcasterSourceProvider implements BroadcasterSourceProvider {

    private final EnvironmentProperties properties;

    public EnvBroadcasterSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<BroadcasterDescriptor> load() {
        return new HashSet<>(properties.broadcasting().broadcasters());
    }

    @Override
    public int priority() {
        return 0;
    }
}
