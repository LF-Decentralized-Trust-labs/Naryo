package io.naryo.infrastructure.configuration.provider.event.store;

import java.util.List;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.store.configuration.provider.StoreSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvStoreSourceProvider implements StoreSourceProvider {

    private final EnvironmentProperties properties;

    public EnvStoreSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<StoreConfigurationDescriptor> load() {
        return List.copyOf(properties.stores());
    }

    @Override
    public int priority() {
        return 1;
    }
}
