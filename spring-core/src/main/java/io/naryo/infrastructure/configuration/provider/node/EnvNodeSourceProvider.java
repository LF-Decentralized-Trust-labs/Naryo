package io.naryo.infrastructure.configuration.provider.node;

import java.util.Collection;
import java.util.HashSet;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.node.configuration.provider.NodeSourceProvider;
import io.naryo.domain.node.connection.http.*;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public final class EnvNodeSourceProvider implements NodeSourceProvider {

    private final EnvironmentProperties properties;

    public EnvNodeSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<NodeDescriptor> load() {
        return new HashSet<>((Collection<? extends NodeDescriptor>) properties.nodes());
    }

    @Override
    public int priority() {
        return 0;
    }
}
