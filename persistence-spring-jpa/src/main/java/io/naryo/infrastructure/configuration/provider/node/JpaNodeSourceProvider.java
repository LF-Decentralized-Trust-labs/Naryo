package io.naryo.infrastructure.configuration.provider.node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.node.configuration.provider.NodeSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodeEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaNodeSourceProvider implements NodeSourceProvider {

    private final NodeEntityRepository repository;

    public JpaNodeSourceProvider(NodeEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<NodeDescriptor> load() {
        List<NodeEntity> nodes = this.repository.findAll();
        return new HashSet<>(nodes);
    }

    @Override
    public int priority() {
        return 0;
    }
}
