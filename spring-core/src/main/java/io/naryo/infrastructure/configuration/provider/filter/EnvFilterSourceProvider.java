package io.naryo.infrastructure.configuration.provider.filter;

import java.util.Collection;
import java.util.HashSet;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.filter.configuration.provider.FilterSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvFilterSourceProvider implements FilterSourceProvider {

    private final EnvironmentProperties properties;

    public EnvFilterSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<FilterDescriptor> load() {
        return new HashSet<>((Collection<? extends FilterDescriptor>) properties.filters());
    }

    @Override
    public int priority() {
        return 1;
    }
}
