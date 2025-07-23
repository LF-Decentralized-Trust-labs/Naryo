package io.naryo.infrastructure.configuration.provider.node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.node.configuration.provider.NodeSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class MongoNodeSourceProvider implements NodeSourceProvider {

    private final NodePropertiesDocumentRepository repository;

    public MongoNodeSourceProvider(NodePropertiesDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<NodeDescriptor> load() {
        List<NodePropertiesDocument> nodes = this.repository.findAll();
        return new HashSet<>(nodes);
    }

    @Override
    public int priority() {
        return 0;
    }
}
