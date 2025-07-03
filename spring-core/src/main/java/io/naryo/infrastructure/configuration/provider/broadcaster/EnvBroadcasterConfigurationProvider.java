package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.broadcaster.configuration.provider.BroadcasterConfigurationProvider;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterTargetEntryProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.FilterBroadcasterTargetConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvBroadcasterConfigurationProvider implements BroadcasterConfigurationProvider {

    private final EnvironmentProperties properties;

    public EnvBroadcasterConfigurationProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<Broadcaster> load() {
        return properties.broadcasting().broadcasters().stream()
                .map(this::toBroadcaster)
                .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 0;
    }

    private Broadcaster toBroadcaster(BroadcasterTargetEntryProperties props) {
        Destination destination = new Destination(props.destination());
        return new Broadcaster(
                UUID.randomUUID(),
                switch (props.type()) {
                    case BLOCK -> new BlockBroadcasterTarget(destination);
                    case TRANSACTION -> new TransactionBroadcasterTarget(destination);
                    case CONTRACT_EVENT -> new ContractEventBroadcasterTarget(destination);
                    case FILTER -> {
                        FilterBroadcasterTargetConfigurationProperties subProps =
                                (FilterBroadcasterTargetConfigurationProperties)
                                        props.configuration();
                        yield new FilterEventBroadcasterTarget(destination, subProps.filterId());
                    }
                    case ALL -> new AllBroadcasterTarget(destination);
                },
                props.configurationId());
    }
}
