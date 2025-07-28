package io.naryo.infrastructure.configuration.provider.event.store;

import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvEventStoreSourceProvider implements EventStoreSourceProvider {

    private final EnvironmentProperties properties;

    public EnvEventStoreSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public EventStoreConfigurationDescriptor load() {
        return properties.eventStore();
    }

    @Override
    public int priority() {
        return 1;
    }
}
