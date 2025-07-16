package io.naryo.infrastructure.configuration.provider.node;

import java.util.Collection;
import java.util.stream.Collectors;

import io.naryo.application.node.configuration.provider.NodeConfigurationProvider;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocumentRepository;
import io.naryo.infrastructure.configuration.provider.node.mapper.NodePropertiesDocumentMapper;
import org.springframework.stereotype.Component;

@Component
public final class MongoNodeConfigurationProvider implements NodeConfigurationProvider {

    private final NodePropertiesDocumentRepository repository;

    public MongoNodeConfigurationProvider(NodePropertiesDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Node> load() {
        return this.repository.findAll().stream()
                .map(NodePropertiesDocumentMapper::fromDocument)
                .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 1;
    }
}
