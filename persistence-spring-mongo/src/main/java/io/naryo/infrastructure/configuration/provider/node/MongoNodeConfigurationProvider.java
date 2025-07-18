package io.naryo.infrastructure.configuration.provider.node;

import java.util.Collection;
import java.util.HashSet;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.node.configuration.provider.NodeSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class MongoNodeConfigurationProvider implements NodeSourceProvider {

    private final NodePropertiesDocumentRepository repository;

    public MongoNodeConfigurationProvider(NodePropertiesDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<NodeDescriptor> load() {
        return new HashSet<>(this.repository.findAll());
    }

    @Override
    public int priority() {
        return 1;
    }
}
