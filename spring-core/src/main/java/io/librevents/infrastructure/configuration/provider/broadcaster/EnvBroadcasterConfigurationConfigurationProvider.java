package io.librevents.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.stream.Collectors;

import io.librevents.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.librevents.application.broadcaster.configuration.provider.BroadcasterConfigurationConfigurationProvider;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.librevents.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvBroadcasterConfigurationConfigurationProvider
        implements BroadcasterConfigurationConfigurationProvider {

    private final EnvironmentProperties properties;
    private final BroadcasterConfigurationMapperRegistry registry;

    public EnvBroadcasterConfigurationConfigurationProvider(
            EnvironmentProperties properties, BroadcasterConfigurationMapperRegistry registry) {
        this.properties = properties;
        this.registry = registry;
    }

    @Override
    public Collection<BroadcasterConfiguration> load() {
        return properties.broadcasting().configuration().stream()
                .map(this::toBroadcasterConfiguration)
                .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 0;
    }

    private BroadcasterConfiguration toBroadcasterConfiguration(
            BroadcasterConfigurationEntryProperties props) {
        return registry.map(props.type(), props);
    }
}
